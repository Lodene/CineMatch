package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDetailsWithReviewsDto;
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
import fr.cpe.cinematch_backend.entities.enums.MovieActionType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LovedMovieService {

    @Autowired
    private LovedMovieRepository lovedMovieRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieActionHistoryService movieActionHistoryService;

    public void likeMovie(AppUser user, Long movieId) throws GenericNotFoundException {

        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                        "Movie with id '" + movieId + "' couldn't be found"));

        if (lovedMovieRepository.existsByUserAndMovie(user, movie)) {
            // unlike a movie
            lovedMovieRepository.findByUserAndMovie(user, movie)
                    .ifPresent(lovedMovieRepository::delete);
            movieActionHistoryService.logAction(user.getId(), movieId, MovieActionType.UNLIKE);
        } else {
            // like a movie
            LovedMovieEntity lovedMovie = new LovedMovieEntity();
            lovedMovie.setUser(user);
            lovedMovie.setMovie(movie);
            lovedMovieRepository.save(lovedMovie);
            movieActionHistoryService.logAction(user.getId(), movieId, MovieActionType.LIKE);
        }
    }

    public List<MovieDetailsWithReviewsDto> getCurentUserLovedMovies(AppUser user) throws GenericNotFoundException {
        List<MovieDto> userMovies = lovedMovieRepository.findByUser(user).stream()
                .map(lovedMovie -> MovieMapper.INSTANCE.toMovieDto(lovedMovie.getMovie()))
                .toList();
        List<MovieDetailsWithReviewsDto> lovedMovie = new ArrayList<>();
        for (MovieDto movieDto : userMovies) {
            lovedMovie.add(new MovieDetailsWithReviewsDto(movieDto, false, true, false, false, new ArrayList<>()));
        }
        return lovedMovie;
    }

    public List<MovieDetailsWithReviewsDto> getLovedMoviesByUsername(String username) throws GenericNotFoundException {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new GenericNotFoundException(404, "User not found",
                    "The user ' " + username + "' is not logged in or does not exist");
        }
        List<LovedMovieEntity> lovedMovieEntityList = lovedMovieRepository.findByUser(user.get());
        List<MovieDto> movieDtoList = new ArrayList<>();
        for (LovedMovieEntity lovedMovieEntity : lovedMovieEntityList) {
            movieDtoList.add(MovieMapper.INSTANCE.toMovieDto(lovedMovieEntity.getMovie()));
        }
        List<MovieDetailsWithReviewsDto> lovedMovie = new ArrayList<>();
        for (MovieDto movieDto : movieDtoList) {
            lovedMovie.add(new MovieDetailsWithReviewsDto(movieDto, false, true, false, false, new ArrayList<>()));
        }
        return lovedMovie;
    }

    public void deleteAllByUserId(Long userId) throws GenericNotFoundException {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with id '" + userId + "' not found"));

        List<LovedMovieEntity> lovedMovies = lovedMovieRepository.findByUser(user);
        lovedMovieRepository.deleteAll(lovedMovies);
    }

    // used for recommendation
    public List<MovieEntity> getRecentlyLikedMovies(String username, int limit) throws GenericNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with username '" + username + "' not found"));
        List<LovedMovieEntity> lovedMovieEntityList = lovedMovieRepository.findByUser(user).stream()
                .sorted(Comparator.comparing(LovedMovieEntity::getLovedAt).reversed())
                .limit(limit)
                .toList();
        return lovedMovieEntityList.stream().map(LovedMovieEntity::getMovie).collect(Collectors.toList());
    }

    public void likeMultipleMovies(AppUser user, List<Long> movieIds) throws GenericNotFoundException {
        for (Long movieId : movieIds) {
            // Vérifie si le film existe
            MovieEntity movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                            "Film introuvable avec l'ID : " + movieId));

            // Vérifie si l'utilisateur a déjà liké ce film
            boolean alreadyLiked = lovedMovieRepository.existsByUserAndMovie(user, movie);
            if (!alreadyLiked) {
                LovedMovieEntity entity = new LovedMovieEntity();
                entity.setUser(user);
                entity.setMovie(movie);
                lovedMovieRepository.save(entity);
            }
        }
    }

}
