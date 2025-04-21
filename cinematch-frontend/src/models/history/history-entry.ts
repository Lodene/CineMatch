import { MovieActionType } from "./movie-action-type";

export class HistoryEntry {
    movieId: number;
    actionType: MovieActionType;
    timestamp: Date;
}