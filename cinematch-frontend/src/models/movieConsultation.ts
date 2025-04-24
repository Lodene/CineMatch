import { Movie } from "./movie";
import { Review } from "./review";

export class MovieConsultation {
    movie: Movie;
    reviews: Review[];
    commented: boolean;
    loved: boolean;
    inWatchlist: boolean;
    watched: boolean;
}
