package fr.cpe.cinematch_backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatchlistMovieRequestDto {
    @NotNull
    private Long idMovie;
}
