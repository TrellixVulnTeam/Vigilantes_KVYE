from keras.models import load_model
import tensorflowjs

if __name__ == "__main__":
    model = load_model("checkpoints/best.h5")
    tensorflowjs.converters.save_keras_model(model,"final_models/js_model")

# tensorflowjs_converter --input_format=keras --output_format=tfjs_graph_model /checkpoints/best.h5 /final_models/js_model
