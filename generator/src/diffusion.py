from flask import Flask, request, send_file
from PIL import Image
import io, os
from diffusers import StableDiffusionPipeline

app = Flask(__name__)

pipe = StableDiffusionPipeline.from_single_file(os.path.abspath("./picxReal_10.safetensors")).to("cuda")

@app.route('/generate', methods=['GET'])
def generate_image():
    # Extract parameters from the request query string
    prompt = request.args.get('prompt', default='man', type=str)
    neg_p = request.args.get('negative_prompt', default='ugly', type=str)
    height = request.args.get('height', default=512, type=int)
    width = request.args.get('width', default=512, type=int)
    steps = request.args.get('num_inference_steps', default=20, type=int)
    guidance = request.args.get('guidance_scale', default=7.5, type=float)


    # Generate the image using parameters
    
    image = pipe(prompt=prompt, height=height, width=width, num_inference_steps=steps, guidance_scale=guidance, negative_prompt=neg_p).images[0]
 
    # Save the image to a byte buffer
    img_byte_array = io.BytesIO()
    image.save(img_byte_array, format='PNG')
    img_byte_array.seek(0)

    # Return the image as response
    return send_file(img_byte_array, mimetype='image/png')

if __name__ == '__main__':
    app.run(debug=True)