from pathlib import Path
import urllib.request
import urllib
import imghdr
import posixpath
import re
import concurrent.futures
import os
from threading import Lock
from s3_helper import write_to_s3

DEST_BUCKET_NAME = os.environ['DEST_BUCKET_NAME']


class Bing:
    def __init__(self, query, limit, output_dir, adult, timeout,  filters='', verbose=True):
        self.lock = Lock()
        self.visited_images = 0
        self.query = query
        self.output_dir = output_dir
        self.adult = adult
        self.filters = filters
        self.verbose = verbose

        assert type(limit) == int, "limit must be integer"
        self.limit = limit
        assert type(timeout) == int, "timeout must be integer"
        self.timeout = timeout

        # self.headers = {'User-Agent': 'Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0'}
        self.page_counter = 0
        self.headers = {'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) '
                        'AppleWebKit/537.11 (KHTML, like Gecko) '
                        'Chrome/23.0.1271.64 Safari/537.11',
                        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
                        'Accept-Charset': 'ISO-8859-1,utf-8;q=0.7,*;q=0.3',
                        'Accept-Encoding': 'none',
                        'Accept-Language': 'en-US,en;q=0.8',
                        'Connection': 'keep-alive'}

    def save_image(self, link, file_path):
        request = urllib.request.Request(link, None, self.headers)
        image = urllib.request.urlopen(request, timeout=self.timeout).read()
        if not imghdr.what(None, image):
            print('[Error]Invalid image, not saving {}\n'.format(link))
            raise ValueError('Invalid image, not saving {}\n'.format(link))
        write_to_s3(DEST_BUCKET_NAME, str(file_path), image)

    def download_image(self, link):
        visited_images = 0
        with self.lock:
            if self.visited_images >= self.limit:
                return
            self.visited_images += 1
            visited_images = self.visited_images
        # Get the image link
        try:
            path = urllib.parse.urlsplit(link).path
            filename = posixpath.basename(path).split('?')[0]
            file_type = filename.split(".")[-1]
            if file_type.lower() not in ["jpe", "jpeg", "jfif", "exif", "tiff", "gif", "bmp", "png", "webp", "jpg"]:
                file_type = "jpg"

            if self.verbose:
                # Download the image
                print("[%] Downloading Image #{} from {}".format(
                    visited_images, link))

            self.save_image(link, self.output_dir.joinpath("Image_{}.{}".format(
                str(visited_images), file_type)))
            if self.verbose:
                print("[%] File Downloaded !\n")

        except Exception as e:
            print("[!] Issue getting: {}\n[!] Error:: {}".format(link, e))

    def run(self):
        num_workers = min(100, self.limit)
        with concurrent.futures.ThreadPoolExecutor(max_workers=num_workers) as executor:

            while True:
                with self.lock:
                    if self.visited_images >= self.limit:
                        return
                if self.verbose:
                    print('\n\n[!!]Indexing page: {}\n'.format(
                        self.page_counter + 1))
                # Parse the page source and download pics
                request_url = 'https://www.bing.com/images/async?q=' + urllib.parse.quote_plus(self.query) \
                    + '&first=' + str(self.page_counter) + '&count=' + str(self.limit) \
                    + '&adlt=' + self.adult + '&qft=' + \
                    ('' if self.filters is None else str(self.filters))
                request = urllib.request.Request(
                    request_url, None, headers=self.headers)
                response = urllib.request.urlopen(request)
                html = response.read().decode('utf8')
                if html == "":
                    print("[%] No more images are available")
                    break
                links = re.findall('murl&quot;:&quot;(.*?)&quot;', html)
                if self.verbose:
                    print("[%] Indexed {} Images on Page {}.".format(
                        len(links), self.page_counter + 1))
                    print("\n===============================================\n")

                for link in links:
                    with self.lock:
                        if self.visited_images >= self.limit:
                            return
                    executor.submit(self.download_image, link)

                self.page_counter += 1


def lambda_handler(event, context):
    term = str(event['term'])
    run_time = event['run_time']
    depth = event['depth']
    downloader = Bing(term, depth, Path(run_time) / term, 'off', 60, True)
    downloader.run()
    return {
        'run_time': run_time
    }
