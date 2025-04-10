package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.requests.MovieCreationRequest;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.MovieMapper;
import fr.cpe.cinematch_backend.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;



    public MovieDto getMovieById(long id) throws GenericNotFoundException {
        Optional<MovieEntity> movieEntity = movieRepository.findById(id);
        if (movieEntity.isPresent()) {
            return MovieMapper.INSTANCE.toMovieDto(movieEntity.get());
        } else {
            throw new GenericNotFoundException(404, "Not found", "No movie were found with the given criterias");
        }
    }

    public MovieDto createMovie(MovieCreationRequest movieCreationRequest) {
        MovieEntity movieEntity = MovieMapper.INSTANCE.movieCreationRequestToMovieEntity(movieCreationRequest);
        this.movieRepository.save(movieEntity);
        return MovieMapper.INSTANCE.toMovieDto(movieEntity);
    }

    public MovieDto updateMovie(long id, MovieDto movieDto) throws GenericNotFoundException{
        Optional<MovieEntity> movieEntity = movieRepository.findById(id);
        if (movieEntity.isEmpty()) {
            throw new GenericNotFoundException(404, "Not found", "The movie was not found");
        }
        movieEntity.get().setTitle(movieDto.getTitle());
        movieEntity.get().setDescription(movieDto.getDescription());
        movieEntity.get().setReleaseDate(movieDto.getReleaseDate());
        movieEntity.get().setPoster(movieDto.getPoster());
        return MovieMapper.INSTANCE.toMovieDto(movieRepository.save(movieEntity.get()));
    }

    public Boolean deleteMovie(long id) {
        Optional<MovieEntity> movieEntity = this.movieRepository.findById(id);
        if (movieEntity.isPresent()) {
            this.movieRepository.delete(movieEntity.get());
            return true;
        } else {
            return false;
        }
    }
}
