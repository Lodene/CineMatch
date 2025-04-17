import { Movie } from "./movie";
import { Review } from "./review";

export class MovieConsultation {
    movie: Movie;
    reviews: Review[];
    isCommented: boolean;
    loved: boolean;
    inWatchlist: boolean;
}