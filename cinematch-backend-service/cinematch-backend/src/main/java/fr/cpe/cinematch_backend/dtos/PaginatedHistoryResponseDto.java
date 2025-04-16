package fr.cpe.cinematch_backend.dtos.responses;

import fr.cpe.cinematch_backend.entities.enums.MovieActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginatedHistoryResponseDto {
    private List<HistoryEntryDto> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HistoryEntryDto {
        private Long movieId;
        private MovieActionType actionType;
        private LocalDateTime timestamp;
    }
}
