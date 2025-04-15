package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.requests.MovieCreationRequest;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.MovieMapper;
import fr.cpe.cinematch_backend.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;


    @Autowired
    private MoviePosterApiService moviePosterApiService;


    public List<MovieDto> getAllMovies() throws GenericNotFoundException {
        List<MovieEntity> movies = movieRepository.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();
        if (!movies.isEmpty()) {
            movies.forEach(
                    movieEntity -> {
                        // Fetch movie poster URL if it's not already set
                        if (movieEntity.getPosterPath() == null || movieEntity.getPosterPath().isEmpty()) {
                            try {
                                String posterUrl = moviePosterApiService.getMoviePosterUrl(movieEntity.getId());
                                movieEntity.setPosterPath(posterUrl);
                                this.movieRepository.save(movieEntity);
                            }catch (GenericNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if (movieEntity.getBackdropPath() == null || movieEntity.getBackdropPath().isEmpty()) {
                            try {
                                String backdropUrl = moviePosterApiService.getMovieBackdropUrl(movieEntity.getId());
                                movieEntity.setBackdropPath(backdropUrl);
                                this.movieRepository.save(movieEntity);
                            }catch (GenericNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        MovieDto movieDto = MovieMapper.INSTANCE.toMovieDto(movieEntity);
                        movieDtos.add(movieDto);
                    }
            );
        }
        return movieDtos;
    }

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
        movieEntity.get().setOverview(movieDto.getOverview());
        movieEntity.get().setReleaseDate(movieDto.getReleaseDate());
        movieEntity.get().setPosterPath(movieDto.getPosterPath());
        movieEntity.get().setImdbRating(movieDto.getImdbRating());
        movieEntity.get().setGenres(movieDto.getGenres());
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


    public Optional<MovieEntity> getMovieEntityById(long id) {
        return this.movieRepository.findById(id);
    }
}
