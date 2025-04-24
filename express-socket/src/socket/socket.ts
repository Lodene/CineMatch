import { Socket, Server as SocketIOServer } from 'socket.io';
import { setNotifNamespace } from '../routes';

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
  
  // private namespace =>
  /*const privateChatNamespace = io.of("/private");

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
  */


  // notif namespace =>
  const notifNamespace = io.of('/notif');
  setNotifNamespace(notifNamespace);


  notifNamespace.on('connection', (socket: Socket) => {
    console.log('Un client est connecté au namespace /notif');

    socket.on("register", (username: string) => {
      console.log(`User registered: ${username} with socket ID ${socket.id}`);
      globalUsers.set(username,
        {
          id: username,
          socketId: socket.id
        });
        console.log(`User registered: ${username} - Socket: ${socket.id}`);
    })
  
    // Gérer la déconnexion du client
    socket.on('disconnect', () => {
      console.log('Client déconnecté du namespace /notif');
    });
  });


  return io;
};
