from flask import request, send_file
import io, os
from . import app
from diffusers import StableDiffusionPipeline

# Load all models from the models folder
models = {}
models_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '../models'))
for filename in os.listdir(models_dir):
    if filename.endswith('.safetensors'):
        model_name = filename.split('.')[0]
        model_path = os.path.join(models_dir, filename)
        models[model_name] = StableDiffusionPipeline.from_single_file(model_path).to("cuda")

@app.route('/generate', methods=['GET'])
def generate_image():
    # Extract parameters from the request query string
    model_name = request.args.get('model', default='picxReal_10', type=str)
    prompt = request.args.get('prompt', default='man', type=str)
    neg_p = request.args.get('negative_prompt', default='ugly', type=str)
    height = request.args.get('height', default=512, type=int)
    width = request.args.get('width', default=512, type=int)
    steps = request.args.get('num_inference_steps', default=20, type=int)
    guidance = request.args.get('guidance_scale', default=7.5, type=float)

    # Check if the requested model exists
    if model_name not in models:
        return f"Model {model_name} not found.", 404

    # Generate the image using the selected model and parameters
    pipe = models[model_name]
    image = pipe(prompt=prompt, height=height, width=width, num_inference_steps=steps, guidance_scale=guidance, negative_prompt=neg_p).images[0]

    # Save the image to a byte buffer
    img_byte_array = io.BytesIO()
    image.save(img_byte_array, format='PNG')
    img_byte_array.seek(0)

    # Return the image as response
    return send_file(img_byte_array, mimetype='image/png')