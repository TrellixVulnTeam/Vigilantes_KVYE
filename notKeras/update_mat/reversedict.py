import json
def invertJSON(mappings):
    switched = {}
    for num,car in mappings.items():
        switched[car] = num
    return switched

if __name__ == "__main__":
    with open("updated_labels.json",'r') as f:
        mappings = json.load(f)
    mappings = invertJSON(mappings)
    with open('final_labels.json','w+') as fp:
        json.dump(mappings,fp)