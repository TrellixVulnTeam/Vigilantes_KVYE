package edu.umich.Vigilantes

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import edu.umich.Vigilantes.R
import android.graphics.BitmapFactory
import android.widget.Toast
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import android.os.Environment
import android.Manifest.permission
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import edu.umich.Vigilantes.databinding.ActivityPdfBinding
import edu.umich.Vigilantes.pdfActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.util.Log
import java.util.*
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.databinding.ktx.BuildConfig
import java.lang.Exception


// A lot of the this activity was adapted from Geeks for Geeks
// Found at https://www.geeksforgeeks.org/how-to-generate-a-pdf-file-in-android-app/
// Credit to the author at https://auth.geeksforgeeks.org/user/chaitanyamunje/profile

class pdfActivity : AppCompatActivity() {
    private lateinit var view: ActivityPdfBinding

    // variables for our buttons.
    private lateinit var generatePDFbtn: Button
    private var finalReport: reportObj? = reportObj()

    // declaring width and height
    // for our PDF file.
    var pageHeight = 1120
    var pagewidth = 792

    // creating a bitmap variable
    // for storing our images
    var bmp: Bitmap? = null
    var scaledbmp: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityPdfBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_pdf)

        // initializing pdf button
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF)

        finalReport = intent.extras?.getParcelable("report")


        //if (!checkPermission()) {
        requestPermission()
        //}
        view.idBtnGeneratePDF.setOnClickListener { // genPDF
            generatePDF()
        }

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

    }

    fun buttonPDF(view: View) = generatePDF()


    fun generatePDF() {
        // Create PDF
        val pdfDocument = PdfDocument()

        val paint = Paint()
        val title = Paint() // for title
        val report = Paint() // should be same for all reports

        // create title page
        val titlePageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()



        // below line is used for setting
        // start page for our PDF file.
        val titlePage = pdfDocument.startPage(titlePageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        val canvas = titlePage.canvas

        //canvas.drawBitmap(scaledbmp!!, 56f, 40f, paint) // this is from GFG, used to make an image

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)


        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15f


        // below line is used for setting color
        // of our text inside our PDF file.
        title.color = ContextCompat.getColor(this, R.color.black)
        val location = findViewById<TextView>(R.id.locationPreview)

        //Display report information
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        val gcd = Geocoder(
            baseContext,
            Locale.getDefault()
        )
        var addresses: List<Address?>

        try {
            if (gps_loc != null) {
                addresses = gcd.getFromLocation(
                    gps_loc.latitude,
                    gps_loc.longitude, 1
                )
                if (addresses.size > 0) {
                    val address =
                        addresses[0]!!.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    val locality = addresses[0]!!.locality
                    val street = addresses[0]!!.thoroughfare
                    val state = addresses[0]!!.adminArea
                    val country = addresses[0]!!.countryName
                    val postalCode = addresses[0]!!.postalCode
                    canvas.drawText("$street $locality $state, $postalCode $country", 120f, 80f, title)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val sdf = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
        val dateTime = sdf.format(Date())
        canvas.drawText(dateTime, 56f, 100f, title)
        //canvas.drawText(location, 56f, 80f, title)
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.textSize = 18f
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("CAR REPORT", 396f, 560f, title)
        pdfDocument.finishPage(titlePage)

        val pages = mutableListOf<PdfDocument.PageInfo>()
        var number = 2
         // initialize page of reports
        finalReport?.let{
            for (vehicle in it.vehicleList){
                pages.add(PageInfo.Builder(pagewidth, pageHeight, number).create())
                number += 1
            }
        }

        report.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        report.textSize = 15f
        report.color = ContextCompat.getColor(this, R.color.black)
        var pagenum = 0
        for(page in pages){
            val startedPage = pdfDocument.startPage(page)
            val reportCanvas = startedPage.canvas
            //carReportTemplate[num]["Name"]?.let { reportCanvas.drawText(it,40f, 80f, report) }
            //carReportTemplate[num]["LicensePlate"]?.let { reportCanvas.drawText(it,40f, 120f, report) }
            finalReport?.let{
                if(pagenum < it.vehicleList.size) {
                    val vehicle = it.vehicleList[pagenum]
                    vehicle.year?.let {
                        reportCanvas.drawText("Year: " + it, 120f, 160f, report)
                    }
                    vehicle.makemodel?.let {
                        reportCanvas.drawText("Model: " + it, 120f, 200f, report)
                    }
                    vehicle.plateNumber?.let {
                        reportCanvas.drawText("Plate: " + it, 120f, 240f, report)
                    }
                    vehicle.VIN?.let {
                        reportCanvas.drawText("VIN: " + it, 120f, 280f, report)
                    }
                    vehicle.color?.let {
                        reportCanvas.drawText("Color: " + it, 120f, 320f, report)
                    }
                }
                if(pagenum < it.participantList.size){
                    val participant = it.participantList[pagenum]
                    participant.name.let{
                        reportCanvas.drawText("Participant name: " + it, 120f, 360f, report)
                    }
                }
                if(pagenum < it.witnessList.size){
                    val witness = it.witnessList[pagenum]
                }



            }
            pagenum += 1
            pdfDocument.finishPage(startedPage)
         }

        // below line is used to set the name of
        // our PDF file and its path.
        val pdfDetails = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME,"report.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE,"application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOWNLOADS)
        }
        try {
            // Try writing pdf file to documents on phone
            val pdfUri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI,pdfDetails)
            pdfUri?.let{
                val stream = contentResolver.openOutputStream(it)
                if (stream == null) {
                    throw IOException("Failed to get output stream.");
                }
                pdfDocument.writeTo(stream)
                stream.close()
            }
            Toast.makeText(this@pdfActivity, "PDF file generated successfully.", Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this@pdfActivity, "Error generating PDF", Toast.LENGTH_SHORT)
                .show()
        }
        // Close the document
        pdfDocument.close()
    }

    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext, permission.READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (!writeStorage || !readStorage) {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun buttonShareFile(view: View?) {
        try {
            val pdfDetails = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "report.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val pdfUri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI,pdfDetails)

            val intentShare = Intent(Intent.ACTION_SEND)
            intentShare.putExtra(
                Intent.EXTRA_STREAM,
                pdfUri
            )
            intentShare.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            intentShare.type = "application/pdf"
            startActivity(Intent.createChooser(intentShare, "Share Generated PDF"))

            Toast.makeText(this@pdfActivity, "PDF file shared successfully.", Toast.LENGTH_SHORT)
                .show()

        }catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(this@pdfActivity, "Error sharing PDF", Toast.LENGTH_SHORT)
            .show()
        }

    }

    companion object {
        // constant code for runtime permissions
        private const val PERMISSION_REQUEST_CODE = 200
    }
}