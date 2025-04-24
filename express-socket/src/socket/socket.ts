import { Socket, Server as SocketIOServer } from 'socket.io';

interface Message {

  content: string;
  sender: string;
  timestamp: EpochTimeStamp;

}

interface User {
  id: string;
  socketId: string;
}

const users: string[] = [];

const privateUsers = new Map<string, User>();

export const globalUsers = new Map<string, User>();

export const initSocket = (server: any) => {
  
  const io = new SocketIOServer(server, {
    cors: {
      origin: "*",
      methods: ["GET", "POST"],
    },
  });
  
  // chat namespace =>
  const chatNamespace = io.of("/chat");
  
  chatNamespace.on('connection', (socket: Socket) => {
    // console.log('Client connected:', socket.id);
    if (!users.find(el => el === socket.handshake.auth.username)) {
      users.push(socket.handshake.auth.username);
    }

    socket.on('join', (userId: string) => {
      // ?
    });

    socket.on("private-message", ({ sender, receiver, message }) => {
      console.log("received", sender, receiver, message);
      const receiverSocketId = users[receiver];
      if (receiverSocketId) {
        socket.to(receiverSocketId).emit("receive-message", { sender, message });
      }
    });

    socket.on("send-all-players", (socket: Socket) => {
      chatNamespace.emit("player-list", users);
    });

    socket.on("disconnect", () => {
      
      for (let i = 0; i < users.length; i++) {
        if (users[i] === socket.handshake.auth.username) {
          delete users[i];
          break;
        }
      }
      console.log("User disconnected:", socket.id);
      chatNamespace.emit("player-list", users);
    });

    socket.on("message", async (data: Message) => {
      // const username: string = getuserNameFromToken(data.sender);
      const username = data.sender;
      console.log("Message received:", data, username);

      // Broadcast the message to all connected clients
      chatNamespace.emit("message", {
        content: data.content,
        sender: username,
        timestamp: data.timestamp,
      });
      
    });

  });






  // private namespace =>
  const privateChatNamespace = io.of("/private");

  privateChatNamespace.on('connection', (socket: Socket)=> {
    console.log(`User connected to private namespace: ${socket.id}`);
    
    socket.on("register", (username: string) => {
      console.log(`User registered: ${username} with socket ID ${socket.id}`);
      privateUsers.set(username,
        {
          id: username,
          socketId: socket.id
        });
        console.log(`User registered: ${username} - Socket: ${socket.id}`);
    });

    socket.on("private-message", ({senderId, receiverId, message}) =>  {
      console.log(message);
      const receiver = privateUsers.get(receiverId);
      if (receiver) {
        privateChatNamespace.to(receiver.socketId).emit("receive-message", {senderId: senderId, message: {
          content: message.content,
          timestamp: message.timestamp,
          sender: message.sender
        }});
      }
    });

    socket.on('disconnect', ()=>{
      privateUsers.forEach((user, userId) => {
        if (user.socketId === socket.id) {
          privateUsers.delete(userId);
        }
      });
      console.log(`User disconnected: ${socket.id}`);
    });
  });


  return io;
};
