FROM python:alpine

WORKDIR /app

COPY ./requirements.txt /app/requirements.txt

RUN pip install --no-cache-dir --upgrade -r /app/requirements.txt

COPY ./main.py /app/main.py

EXPOSE 8000

CMD gunicorn main:app --bind :8000 --log-level=trace -w 4 -k uvicorn.workers.UvicornWorker