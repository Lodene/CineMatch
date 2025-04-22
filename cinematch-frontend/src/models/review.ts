export class Review {
    id: number;
    username: string = "";
    idMovie: number = 1;
    movieTitle: string = "TEst";
    note: number = 9.1;
    description: string;
    createdAt: Date = new Date();
    modifiedAt: Date = new Date();
}