from classifier import predict, licence_plate

labels = {'Car', 'Licence Plate', 'VIN', 'Miscellaneous'}

def task(img, label):
    """
    Assigns picture to the proper file depending on its label type
    """
    if label not in labels:
        raise RuntimeError(label, ' is not a valid label')
    if label == 'Car':
        args = '' # TODO
        top_three = predict.predict(args)
        return top_three
    elif label == 'Licence Plate':
        lpn = licence_plate.lpn_predict(img)
        return lpn
    elif label == 'VIN':
        # Same lpn_predict function ?
        pass 
    else:
        return None

if '__name__' == __main__:
    print('Hello world')
