# celery_worker/tasks.py
from celery_app import app
import os
import stomp

ACTIVEMQ_HOST = os.getenv("ACTIVEMQ_HOST", "activemq")
ACTIVEMQ_PORT = int(os.getenv("ACTIVEMQ_PORT", 61613))
ACTIVEMQ_USER = os.getenv("ACTIVEMQ_USER", "admin")
ACTIVEMQ_PASS = os.getenv("ACTIVEMQ_PASS", "cine")


@app.task
def process_message(message):
    print(f"Processing: {message}")
    send_response(f"Processed: {message}")

def send_response(msg):
    conn = stomp.Connection([(ACTIVEMQ_HOST, ACTIVEMQ_PORT)])
    conn.connect(ACTIVEMQ_USER, ACTIVEMQ_PASS, wait=True)
    conn.send(body=msg, destination='/queue/outbox')
    conn.disconnect()
