import { MovieActionType } from "./movie-action-type";

export class HistoryEntry {
    movieId: number;
    actionType: MovieActionType;
    /**
     * Timestamp is non localized from the backend, it must first be parsed (see timeUtils.ts)
     */
    timestamp: string;
    movieTitle: string;
}