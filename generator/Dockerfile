FROM nvidia/cuda:12.6.2-runtime-ubuntu24.04

RUN apt update && apt install -y python3.12 python3-pip

WORKDIR /app/generator

COPY ./requirements.txt .

RUN pip install -r requirements.txt --break-system-packages

COPY ./src ./src

ENV PYTHONPATH=/app/generator/src

EXPOSE 5000

CMD ["python3", "-m", "src.main"]