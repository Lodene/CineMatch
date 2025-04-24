import { Movie } from "./movie";

export class PaginatedMovieResponse {
    content: Movie[];
    currentPage: number;
    totalPages: number
    totalElements: number
    hasNext: boolean;
}