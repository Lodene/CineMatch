import express from "express";
import { Request, Response, NextFunction } from "express";
import { Namespace } from "socket.io";
import { globalUsers } from "../socket/socket";

//define routes 
const router = express.Router();
interface ErrorResponse {

}


router.post("/recommended-film", function (req: Request, res: Response, next: NextFunction) {
    console.log(req, res);
    if (req.body !== null && req.body !== undefined) {
        // notify frontend of the update
    }
    res.send("roger")
});


export default router;