package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.WatchedMovieEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.MovieMapper;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.MovieRepository;
import fr.cpe.cinematch_backend.repositories.WatchedMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchedMovieService {

        @Autowired
        private WatchedMovieRepository watchedMovieRepository;

        @Autowired
        private AppUserRepository appUserRepository;

        @Autowired
        private MovieRepository movieRepository;

        public void addOrRemoveWatchedMovie(Long movieId, String username) throws GenericNotFoundException {
                AppUser user = appUserRepository.findByUsername(username)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "username '" + username + "' not found"));

                MovieEntity movie = movieRepository.findById(movieId)
                                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                                                "movie ID '" + movieId + "' not found"));

                watchedMovieRepository.findByUserAndMovie(user, movie)
                                .ifPresentOrElse(
                                                watchedMovieRepository::delete,
                                                () -> {
                                                        WatchedMovieEntity entity = new WatchedMovieEntity();
                                                        entity.setUser(user);
                                                        entity.setMovie(movie);
                                                        watchedMovieRepository.save(entity);
                                                });
        }

        public List<MovieDto> getWatchedMoviesByUsername(String username) throws GenericNotFoundException {
                AppUser user = appUserRepository.findByUsername(username)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "username '" + username + "' not found"));

                return watchedMovieRepository.findByUser(user).stream()
                                .map(entry -> MovieMapper.INSTANCE.toMovieDto(entry.getMovie()))
                                .toList();
        }

        public List<MovieDto> getWatchedMoviesByUserId(Long idUser) throws GenericNotFoundException {
                AppUser user = appUserRepository.findById(idUser)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "user ID '" + idUser + "' not found"));

                return watchedMovieRepository.findByUser(user).stream()
                                .map(entry -> MovieMapper.INSTANCE.toMovieDto(entry.getMovie()))
                                .toList();
        }

        public void deleteAllByUserId(Long userId) throws GenericNotFoundException {
                AppUser user = appUserRepository.findById(userId)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "User with id '" + userId + "' not found"));

                List<WatchedMovieEntity> watchedMovies = watchedMovieRepository.findByUser(user);
                watchedMovieRepository.deleteAll(watchedMovies);
        }

}
