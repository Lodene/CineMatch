# celery_worker/celery_app.py
from celery import Celery
import os

redis_url = os.getenv('REDIS_URL', 'redis://localhost:6379/0')
app = Celery('tasks', broker=redis_url)

# Import tasks so they get registered
import tasks
