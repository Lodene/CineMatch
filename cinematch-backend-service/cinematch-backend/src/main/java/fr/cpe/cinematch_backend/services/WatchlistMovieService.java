package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDetailsWithReviewsDto;
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
import fr.cpe.cinematch_backend.entities.enums.MovieActionType;

import java.util.ArrayList;
import java.util.List;

@Service
public class WatchlistMovieService {

        @Autowired
        private WatchlistMovieRepository watchlistMovieRepository;

        @Autowired
        private AppUserRepository appUserRepository;

        @Autowired
        private MovieRepository movieRepository;

        @Autowired
        private MovieActionHistoryService movieActionHistoryService;

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
                                existing -> {
                                        watchlistMovieRepository.delete(existing);
                                        // Ajout dans l'historique : WATCHLIST_REMOVE
                                        movieActionHistoryService.logAction(user.getId(), movieId, MovieActionType.WATCHLIST_REMOVE);
                                },
                                () -> {
                                        WatchlistMovieEntity newEntry = new WatchlistMovieEntity();
                                        newEntry.setUser(user);
                                        newEntry.setMovie(movie);
                                        watchlistMovieRepository.save(newEntry);

                                        // Ajout dans l'historique : WATCHLIST_ADD
                                        movieActionHistoryService.logAction(user.getId(), movieId, MovieActionType.WATCHLIST_ADD);
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

        public List<MovieDetailsWithReviewsDto> getWatchListWithReviewsByUsername(String username) throws GenericNotFoundException {
                List<MovieDto> watchlistMovies = this.getWatchlistByUsername(username);
                List<MovieDetailsWithReviewsDto> watchListMoviesWithDetails = new ArrayList<>();
                for (MovieDto movieDto : watchlistMovies ) {
                        watchListMoviesWithDetails.add(new MovieDetailsWithReviewsDto(movieDto, false, false, true, false, new ArrayList<>()));
                }
                return watchListMoviesWithDetails;
        }

        public List<MovieDto> getWatchlistByUserId(Long userId) throws GenericNotFoundException {
                AppUser user = appUserRepository.findById(userId)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "User with id '" + userId + "' couldn't be found"));

                return watchlistMovieRepository.findByUser(user).stream()
                                .map(entry -> MovieMapper.INSTANCE.toMovieDto(entry.getMovie()))
                                .toList();
        }

        public List<MovieDetailsWithReviewsDto> getWatchListWithReviewsByUserId(Long userId) throws GenericNotFoundException {
                List<MovieDto> watchlistMovies = this.getWatchlistByUserId(userId);
                List<MovieDetailsWithReviewsDto> watchListMoviesWithDetails = new ArrayList<>();
                for (MovieDto movieDto : watchlistMovies ) {
                        watchListMoviesWithDetails.add(new MovieDetailsWithReviewsDto(movieDto, false, false, true, false, new ArrayList<>()));
                }
                return watchListMoviesWithDetails;
        }

        public void deleteAllByUserId(Long userId) throws GenericNotFoundException {
                AppUser user = appUserRepository.findById(userId)
                                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                                                "User with id '" + userId + "' not found"));

                List<WatchlistMovieEntity> watchlist = watchlistMovieRepository.findByUser(user);
                watchlistMovieRepository.deleteAll(watchlist);
        }

}
