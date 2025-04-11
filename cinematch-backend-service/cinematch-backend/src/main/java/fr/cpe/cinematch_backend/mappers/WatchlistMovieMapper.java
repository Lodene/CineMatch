package fr.cpe.cinematch_backend.mappers;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.WatchlistMovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = MovieMapper.class)
public interface WatchlistMovieMapper {

    WatchlistMovieMapper INSTANCE = Mappers.getMapper(WatchlistMovieMapper.class);

    default MovieDto toMovieDto(WatchlistMovieEntity entity) {
        return MovieMapper.INSTANCE.toMovieDto(entity.getMovie());
    }
}
