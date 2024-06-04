# Use an official Python runtime as a parent image
FROM nvidia/cuda:12.4.1-runtime-ubuntu22.04

RUN apt-get update && \
  apt-get install -y python3.11 python3.11-distutils python3-pip python-is-python3 && \
  rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app/generator

# Copy the current directory contents into the container at /app
COPY ./requirements.txt .

# Install any needed dependencies specified in requirements.txt
RUN pip install -r requirements.txt

# Copy only the necessary files from the build stage
COPY . .
# Expose the port Flask will run on
EXPOSE 5000

# Define environment variable
ENV FLASK_APP=diffusion.py

# Run app.py when the container launches
CMD ["flask", "run", "--host=0.0.0.0"]