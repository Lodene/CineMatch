package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.LovedMovieRequestDto;
import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.LovedMovieEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.MovieMapper;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.MovieRepository;
import fr.cpe.cinematch_backend.repositories.LovedMovieRepository;
import fr.cpe.cinematch_backend.services.security.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LovedMovieService {

    @Autowired
    private LovedMovieRepository lovedMovieRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public void likeMovie(Long movieId) throws GenericNotFoundException {

        AppUser user = checkAndRetrieveUser();
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                        "Movie with id '" + movieId + "' couldn't be found"));

        if (lovedMovieRepository.existsByUserAndMovie(user, movie)){
            // unlike a movie
            lovedMovieRepository.findByUserAndMovie(user, movie)
                    .ifPresent(lovedMovieRepository::delete);
        } else {
            // like a movie
            LovedMovieEntity lovedMovie = new LovedMovieEntity();
            lovedMovie.setUser(user);
            lovedMovie.setMovie(movie);
            lovedMovieRepository.save(lovedMovie);
        }
    }

    public List<MovieDto> getCurentUserLovedMovies() throws GenericNotFoundException {
        AppUser user = checkAndRetrieveUser();
        return lovedMovieRepository.findByUser(user).stream()
                .map(lovedMovie -> MovieMapper.INSTANCE.toMovieDto(lovedMovie.getMovie()))
                .toList();
    }

    public List<MovieDto> getLovedMoviesByUsername(String username) throws GenericNotFoundException {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new GenericNotFoundException(404, "User not found", "The user ' "+ username + "' is not logged in or does not exist");
        }
        List<LovedMovieEntity> lovedMovieEntityList = lovedMovieRepository.findByUser(user.get());
        List<MovieDto> movieDtoList = new ArrayList<>();
        for (LovedMovieEntity lovedMovieEntity : lovedMovieEntityList) {
            movieDtoList.add(MovieMapper.INSTANCE.toMovieDto(lovedMovieEntity.getMovie()));
        }
        return movieDtoList;
    }

    private AppUser checkAndRetrieveUser() throws GenericNotFoundException {
        AppUser user = currentUserService.getCurrentUser();
        if (user == null) {
            throw new GenericNotFoundException(404, "User not found", "The current user is not logged in or does not exist");
        }
        return user;
    }
}
