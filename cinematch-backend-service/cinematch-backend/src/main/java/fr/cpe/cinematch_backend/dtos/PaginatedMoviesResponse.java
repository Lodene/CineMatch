package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedMoviesResponse {
    private List<MovieDto> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
}
