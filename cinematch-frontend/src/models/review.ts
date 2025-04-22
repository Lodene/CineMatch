export class Review {
    id: number;
    username: string;
    idMovie: number;
    movieTitle: string;
    note: number = 0.5;
    description: string;
    createdAt: Date = new Date();
    modifiedAt: Date = new Date();
}