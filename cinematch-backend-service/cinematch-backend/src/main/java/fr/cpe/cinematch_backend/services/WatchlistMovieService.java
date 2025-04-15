package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.WatchlistMovieEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.MovieMapper;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import fr.cpe.cinematch_backend.repositories.MovieRepository;
import fr.cpe.cinematch_backend.repositories.WatchlistMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistMovieService {

        @Autowired
        private WatchlistMovieRepository watchlistMovieRepository;

        @Autowired
        private AppUserRepository appUserRepository;

        @Autowired
        private MovieRepository movieRepository;

        /*
         * Add a movie to curent user's watchlist, or remove it if it already exist.
         */
        public void AddOrRemoveMovieFromWatchlist(Long movieId, String username) throws GenericNotFoundException {
                AppUser user = appUserRepository.findByUsername(username)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "User with username '" + username + "' couldn't be found"));

                MovieEntity movie = movieRepository.findById(movieId)
                                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                                                "Movie with id '" + movieId + "' couldn't be found"));

                watchlistMovieRepository.findByUserAndMovie(user, movie)
                                .ifPresentOrElse(
                                                watchlistMovieRepository::delete,
                                                () -> {
                                                        WatchlistMovieEntity newEntry = new WatchlistMovieEntity();
                                                        newEntry.setUser(user);
                                                        newEntry.setMovie(movie);
                                                        watchlistMovieRepository.save(newEntry);
                                                });
        }

        // return watchlist by username
        public List<MovieDto> getWatchlistByUsername(String username) throws GenericNotFoundException {
                AppUser user = appUserRepository.findByUsername(username)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "User with username '" + username + "' couldn't be found"));

                return watchlistMovieRepository.findByUser(user).stream()
                                .map(entry -> MovieMapper.INSTANCE.toMovieDto(entry.getMovie()))
                                .toList();
        }

        public List<MovieDto> getWatchlistByUserId(Long userId) throws GenericNotFoundException {
                AppUser user = appUserRepository.findById(userId)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "User with id '" + userId + "' couldn't be found"));

                return watchlistMovieRepository.findByUser(user).stream()
                                .map(entry -> MovieMapper.INSTANCE.toMovieDto(entry.getMovie()))
                                .toList();
        }

        public void deleteAllByUserId(Long userId) throws GenericNotFoundException {
                AppUser user = appUserRepository.findById(userId)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "User with id '" + userId + "' not found"));

                List<WatchlistMovieEntity> watchlist = watchlistMovieRepository.findByUser(user);
                watchlistMovieRepository.deleteAll(watchlist);
        }

}
