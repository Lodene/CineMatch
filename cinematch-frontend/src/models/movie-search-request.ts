export class MovieSearchRequest {
    title: string | null;
    genres: string[] | null;
    director: string[] | null;
    cast: string[] | null;
    minRating: number | null;
    maxRating: number | null = 10;
    startDate: Date | null;
    endDate: Date | null;
}
