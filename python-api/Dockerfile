FROM python:alpine

WORKDIR /app

COPY ./requirements.txt /app/requirements.txt

RUN pip install --no-cache-dir --upgrade -r /app/requirements.txt

COPY ./main.py /app/main.py

EXPOSE 8001

CMD gunicorn main:app --bind :8001 --log-level=trace -w 1 -k uvicorn.workers.UvicornWorker

# CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--reload"]
