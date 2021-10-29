from keras.models import load_model
import tensorflowjs

if __name__ == "__main__":
    model = load_model("final_models/finale.h5")
    tensorflowjs.converters.save_keras_model(model,"final_models/js_model")

