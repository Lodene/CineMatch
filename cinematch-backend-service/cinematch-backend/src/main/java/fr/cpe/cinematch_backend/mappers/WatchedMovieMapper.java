package fr.cpe.cinematch_backend.mappers;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.WatchedMovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = MovieMapper.class)
public interface WatchedMovieMapper {

    WatchedMovieMapper INSTANCE = Mappers.getMapper(WatchedMovieMapper.class);

    default MovieDto toMovieDto(WatchedMovieEntity entity) {
        return MovieMapper.INSTANCE.toMovieDto(entity.getMovie());
    }
}
