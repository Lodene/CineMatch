FROM python:3.11-slim

WORKDIR /app

COPY . .

RUN pip install --no-cache-dir stomp.py==7.0.0 celery redis

CMD ["python", "listener.py"]