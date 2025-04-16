package fr.cpe.cinematch_backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import fr.cpe.cinematch_backend.entities.MovieActionHistoryEntity;
import fr.cpe.cinematch_backend.entities.enums.MovieActionType;
import fr.cpe.cinematch_backend.repositories.MovieActionHistoryRepository;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.dtos.PaginatedHistoryResponseDto;
import fr.cpe.cinematch_backend.entities.AppUser;

@Service
public class MovieActionHistoryService {

    @Autowired
    private MovieActionHistoryRepository repository;

    @Autowired
    private AppUserRepository appUserRepository;

    public void logAction(Long userId, Long movieId, MovieActionType actionType) {
        MovieActionHistoryEntity history = new MovieActionHistoryEntity();
        history.setUserId(userId);
        history.setMovieId(movieId);
        history.setActionType(actionType);
        repository.save(history);
    }

    public PaginatedHistoryResponseDto getUserHistoryPaginated(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<MovieActionHistoryEntity> pageResult = repository.findByUserIdOrderByTimestampDesc(userId, pageable);

        List<PaginatedHistoryResponseDto.HistoryEntryDto> simplified = pageResult.getContent().stream()
                .map(entry -> new PaginatedHistoryResponseDto.HistoryEntryDto(
                        entry.getMovieId(),
                        entry.getActionType(),
                        entry.getTimestamp()))
                .toList();

        return PaginatedHistoryResponseDto.builder()
                .content(simplified)
                .currentPage(pageResult.getNumber())
                .totalPages(pageResult.getTotalPages())
                .totalElements(pageResult.getTotalElements())
                .hasNext(pageResult.hasNext())
                .build();
    }
}
