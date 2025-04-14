package fr.cpe.cinematch_backend.mappers;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.requests.MovieCreationRequest;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);


    MovieEntity toMovieEntity(MovieDto movieDto);

    MovieDto toMovieDto(MovieEntity movieEntity);

    // used for debug purpose
    @Mapping(target = "id", ignore = true )
    MovieEntity movieCreationRequestToMovieEntity(MovieCreationRequest movieCreationRequest);
}
