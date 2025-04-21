import { HistoryEntry } from "./history-entry";

export class PaginatedHistoryResponse {
    content: HistoryEntry[];
    currentPage: number;
    totalPagese: number;
    totalElementse: number;
    hasNext: boolean;

}
