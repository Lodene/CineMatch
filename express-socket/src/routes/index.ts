import express from "express";
import { Request, Response, NextFunction } from "express";
import { Namespace } from "socket.io";
import { globalUsers } from "../socket/socket";


type SocketRequest = {
    requestId: string;
    fromUsername: string;
    recommendedMovies: any[]
}

//define routes 
const router = express.Router();

let notifNamespace: Namespace;

export const setNotifNamespace = (namespace: Namespace) => {
  notifNamespace = namespace;
};


router.post("/recommended-film", function (req: Request, res: Response, next: NextFunction) {
    // console.log(req, res);
    if (req.body !== null && req.body !== undefined) {
        // notify frontend of the update
        console.log(req.body);
        const { requestId, fromUsername, recommendedMovies }: SocketRequest = req.body;
        if (notifNamespace !== null) {

            const socket = globalUsers.get(fromUsername);
            if (!!socket) {
                const targetSocket = notifNamespace.sockets.get(socket.socketId);
                if (targetSocket !== null && targetSocket !== undefined) {
                    targetSocket.emit('recommendation', { recommendedMovies });
    
                    console.log('Notification envoyée:', { recommendedMovies });
                    res.status(200).send({ message: 'Notification envoyée avec succès' });    
                } else {
                    res.status(404).send({ message: 'Client non trouvé' });
                }
       
            } else {
                res.status(500).send({ message: 'Socket non initialisé' });
            } 

        } else {
            res.status(500).send({ message: 'Namespace Socket.IO non initialisé' });
        }


    }
    // res.send("roger")
});


export default router;