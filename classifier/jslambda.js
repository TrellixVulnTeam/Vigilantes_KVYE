console.log('Loading function');

const aws = require('aws-sdk');

const s3 = new aws.S3({ apiVersion: '2006-03-01' });
const tf = require('@tensorflow/tfjs');
const jimp = require('jimp')

exports.handler = async (event, context) => {
    //console.log('Received event:', JSON.stringify(event, null, 2));
    const MODEL_URL = `https://dkybhu8tq8.execute-api.us-east-2.amazonaws.com/keyless/car-classifier/model.json`;
    const model = await tf.loadLayersModel(MODEL_URL);
    
    var params = { Bucket: "imageput", Key: "image.jpeg" };
    
    const res = await s3.getObject(params).promise();
    const image = await jimp.read(Buffer.from(res.Body, 'base64'))
    const mime = image.getMIME();
    //const resizedImageBuffer = await image.scaleToFit(224, 224).getBufferAsync(mime);
    const values = image.bitmap.data;
    let outShape = [1, image.bitmap.width, image.bitmap.height, 4];
    let input = tf.tensor4d(values, outShape, 'float32');
    input = input.slice([0, 0, 0, 0], [1, image.bitmap.width, image.bitmap.height, 3])
    input = tf.image.resizeBilinear(input,[224,224])
    const result = model.predict(input); 
    console.log(`Predicted car:${tf.argMax(result,1)}`)
    
    
    
    /*s3.getObject(params, function(err, data) {
        if(err){
            console.log("whoops")
            return
        }
        else{
            console.log('Data empty?')
            console.log(data)
            console.log(typeof(data))
            var input = tf.browser.fromPixels(data)
            console.log(typeof(input));
        }
        
    });
    var modelParams = {
        Bucket: "car-classifier", 
        Key: "model.json"
    };*/

    
    
};