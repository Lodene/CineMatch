import stomp

class MyListener(stomp.ConnectionListener):
    def on_message(self, headers, message):
        print(f"Received message: {message}")

# Create the connection
conn = stomp.Connection([('activemq', 61613)])  # Make sure 'activemq' is the correct hostname

# Set the listener
conn.set_listener('', MyListener())

# Connect with credentials
conn.connect('admin', 'cine', wait=True)

# Subscribe to a destination (queue or topic)
conn.subscribe(destination='/queue/myqueue', id=1, ack='auto')

# Keep the connection open
# conn.start()
# conn.wait()
