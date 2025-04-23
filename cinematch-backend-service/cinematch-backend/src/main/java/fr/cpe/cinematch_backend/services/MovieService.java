package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDetailsWithReviewsDto;
import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.dtos.ReviewWithFriendFlagDto;
import fr.cpe.cinematch_backend.dtos.requests.MovieCreationRequest;
import fr.cpe.cinematch_backend.dtos.requests.MovieSearchRequest;
import fr.cpe.cinematch_backend.entities.*;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.MovieMapper;
import fr.cpe.cinematch_backend.mappers.ReviewMapper;
import fr.cpe.cinematch_backend.mappers.ReviewWithFriendFlagMapper;
import fr.cpe.cinematch_backend.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MoviePosterApiService moviePosterApiService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private WatchlistMovieService watchlistMovieService;

    @Autowired
    private ProfilRepository profilRepository;
    @Autowired
    private LovedMovieRepository lovedMovieRepository;

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
                            } catch (BadEndpointException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if (movieEntity.getBackdropPath() == null || movieEntity.getBackdropPath().isEmpty()) {
                            try {
                                String backdropUrl = moviePosterApiService.getMovieBackdropUrl(movieEntity.getId());
                                movieEntity.setBackdropPath(backdropUrl);
                                this.movieRepository.save(movieEntity);
                            } catch (BadEndpointException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        MovieDto movieDto = MovieMapper.INSTANCE.toMovieDto(movieEntity);
                        movieDtos.add(movieDto);
                    });
        }
        return movieDtos;
    }

    public MovieDetailsWithReviewsDto getMovieDetailsWithReviews(Long movieId, String username)
            throws GenericNotFoundException {
        MovieEntity movie = this.getMovieEntityById(movieId)
                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                        "movie with id '" + movieId + "' not found"));

        List<ReviewEntity> reviews = reviewRepository.findByMovie(movie);

        boolean hasCommented = false, isLoved = false, isInWatchList = false;
        AppUser appUser;
        if (username != null) {
            appUser = appUserRepository.findByUsername(username)
                    .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                            "username '" + username + "' does not exist"));
            hasCommented = reviews.stream().anyMatch(r -> r.getUser().getId() == appUser.getId());
            isLoved = lovedMovieRepository.findByUserAndMovie(appUser, movie).isPresent();
            List<MovieDto> watchListMovie = watchlistMovieService.getWatchlistByUsername(username);
            isInWatchList = watchListMovie.stream().anyMatch(m -> m.getId().equals(movie.getId()));
        } else {
            appUser = null;
        }
        List<ReviewWithFriendFlagDto> enrichedReviews = reviews.stream().map(review -> {
            ProfileEntity profile = profilRepository.findByUserId(review.getUser().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Profile missing for user " + review.getUser().getId()));
            ProfileDto profileDto = ReviewWithFriendFlagMapper.INSTANCE.profileEntityToDto(profile);
            ReviewWithFriendFlagDto dto = ReviewWithFriendFlagMapper.INSTANCE.fromReviewDtoAndProfile(
                    ReviewMapper.INSTANCE.toReviewDto(review),
                    profileDto);
            if (username != null) {
                dto.setWrittenByFriend(friendshipService.isFriend(appUser.getId(), review.getUser().getId()));
            }

            return dto;
        }).toList();

        MovieDto movieDto = MovieMapper.INSTANCE.toMovieDto(movie);

        return new MovieDetailsWithReviewsDto(movieDto, hasCommented, isLoved, isInWatchList, enrichedReviews);
    }

    public MovieDto createMovie(MovieCreationRequest movieCreationRequest) {
        MovieEntity movieEntity = MovieMapper.INSTANCE.movieCreationRequestToMovieEntity(movieCreationRequest);
        this.movieRepository.save(movieEntity);
        return MovieMapper.INSTANCE.toMovieDto(movieEntity);
    }

    public MovieDto updateMovie(long id, MovieDto movieDto) throws GenericNotFoundException {
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

    public Set<String> getAllGenres() {
        List<MovieEntity> movies = movieRepository.findAll();
        Set<String> genres = new HashSet<>();

        for (MovieEntity movie : movies) {
            if (movie.getGenres() != null) {
                genres.addAll(movie.getGenres());
            }
        }

        return genres;
    }

    public List<MovieDto> searchMovies(MovieSearchRequest request) throws GenericNotFoundException {
        if (request == null ||
                (request.getTitle() == null &&
                        (request.getGenres() == null || request.getGenres().isEmpty()) &&
                        (request.getDirector() == null || request.getDirector().isEmpty()) &&
                        (request.getCast() == null || request.getCast().isEmpty()) &&
                        request.getMinRating() == null &&
                        request.getMaxRating() == null &&
                        request.getStartDate() == null &&
                        request.getEndDate() == null)) {
            throw new GenericNotFoundException(400, "Invalid search request",
                    "At least one search criterion must be provided.");
        }

        List<MovieEntity> allMovies = movieRepository.findAll();

        return allMovies.stream()
                .filter(movie -> request.getTitle() == null
                        || movie.getTitle().toLowerCase().contains(request.getTitle().toLowerCase()))
                .filter(movie -> request.getGenres() == null || !request.getGenres().isEmpty() &&
                        movie.getGenres().stream().anyMatch(request.getGenres()::contains))
                .filter(movie -> request.getDirector() == null || !request.getDirector().isEmpty() &&
                        movie.getDirector().stream().anyMatch(request.getDirector()::contains))
                .filter(movie -> request.getCast() == null || !request.getCast().isEmpty() &&
                        movie.getCast().stream().anyMatch(request.getCast()::contains))
                .filter(movie -> request.getMinRating() == null || movie.getImdbRating() >= request.getMinRating())
                .filter(movie -> request.getMaxRating() == null || movie.getImdbRating() <= request.getMaxRating())
                .filter(movie -> request.getStartDate() == null
                        || (movie.getReleaseDate() != null && movie.getReleaseDate().after(request.getStartDate())))
                .filter(movie -> request.getEndDate() == null
                        || (movie.getReleaseDate() != null && movie.getReleaseDate().before(request.getEndDate())))
                .map(MovieMapper.INSTANCE::toMovieDto)
                .toList();
    }

}
