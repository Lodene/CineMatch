package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDetailsWithReviewsDto;
import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.PaginatedMoviesResponse;
import fr.cpe.cinematch_backend.dtos.ProfileDto;
import fr.cpe.cinematch_backend.dtos.ReviewWithFriendFlagDto;
import fr.cpe.cinematch_backend.dtos.requests.MovieCreationRequest;
import fr.cpe.cinematch_backend.dtos.requests.MovieRecommendationRequest;
import fr.cpe.cinematch_backend.dtos.requests.MovieSearchRequest;
import fr.cpe.cinematch_backend.dtos.requests.SocketRequest;
import fr.cpe.cinematch_backend.entities.*;
import fr.cpe.cinematch_backend.exceptions.BadEndpointException;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.MovieMapper;
import fr.cpe.cinematch_backend.mappers.ReviewMapper;
import fr.cpe.cinematch_backend.mappers.ReviewWithFriendFlagMapper;
import fr.cpe.cinematch_backend.repositories.*;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private WatchlistMovieService watchlistMovieService;

    @Autowired
    private WatchedMovieService watchedMovieService;

    @Autowired
    private WatchedMovieRepository watchedMovieRepository;

    @Autowired
    private ProfilRepository profilRepository;
    @Autowired
    private LovedMovieRepository lovedMovieRepository;

    public PaginatedMoviesResponse getAllMoviesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Slice<MovieEntity> movieSlice = movieRepository.findAll(pageable);

        // Page<MovieEntity> moviePage = movieRepository.findAll(pageable);

        List<MovieDto> movieDtos = movieSlice.getContent().stream()
                .map(MovieMapper.INSTANCE::toMovieDto)
                .toList();

        return PaginatedMoviesResponse.builder()
                .content(movieDtos)
                .currentPage(movieSlice.getNumber())
                // .totalPages(movieSlice.getTotalPages())
                // .totalElements(movieSlice.getTotalElements())
                .hasNext(movieSlice.hasNext())
                .build();
    }

    public MovieDetailsWithReviewsDto getMovieDetailsWithReviews(Long movieId, String username)
            throws GenericNotFoundException {
        MovieEntity movie = this.getMovieEntityById(movieId)
                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                        "movie with id '" + movieId + "' not found"));

        List<ReviewEntity> reviews = reviewRepository.findByMovie(movie);

        boolean hasCommented = false, isLoved = false, isInWatchList = false, isWatched = false;
        AppUser appUser;
        if (username != null) {
            appUser = appUserRepository.findByUsername(username)
                    .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                            "username '" + username + "' does not exist"));
            hasCommented = reviews.stream().anyMatch(r -> r.getUser().getId() == appUser.getId());
            isLoved = lovedMovieRepository.findByUserAndMovie(appUser, movie).isPresent();
            isWatched = watchedMovieRepository.findByUserAndMovie(appUser, movie).isPresent();
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

        return new MovieDetailsWithReviewsDto(movieDto, hasCommented, isLoved, isInWatchList, isWatched,
                enrichedReviews);
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
        List<MovieEntity> movies = (List<MovieEntity>) movieRepository.findAll();
        Set<String> genres = new HashSet<>();

        for (MovieEntity movie : movies) {
            if (movie.getGenres() != null) {
                genres.addAll(movie.getGenres());
            }
        }

        return genres;
    }

    public PaginatedMoviesResponse searchMovies(MovieSearchRequest request, int page, int size) throws GenericNotFoundException {
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

        // Create a specification based on the search criteria
        Specification<MovieEntity> specification = buildMovieFilterSpecification(request);

        // Create pageable with sorting
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // Use the specification with pagination
        Page<MovieEntity> moviePage = movieRepository.findAll(specification, pageable);

        // Map entities to DTOs
        List<MovieDto> movieDtos = moviePage.getContent().stream()
                .map(MovieMapper.INSTANCE::toMovieDto)
                .toList();

        // Build the paginated response
        return PaginatedMoviesResponse.builder()
                .content(movieDtos)
                .currentPage(moviePage.getNumber())
                .totalPages(moviePage.getTotalPages())
                .totalElements(moviePage.getTotalElements())
                .hasNext(moviePage.hasNext())
                .build();
    }


    private Specification<MovieEntity> buildMovieFilterSpecification(MovieSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getTitle() != null) {
                predicates.add(cb.like(
                        cb.lower(root.get("title")),
                        "%" + request.getTitle().toLowerCase() + "%"
                ));
            }

            if (request.getGenres() != null && !request.getGenres().isEmpty()) {
                // This implementation depends on how genres are stored in your entity
                // Assuming genres is a collection in the entity
                Join<MovieEntity, String> genresJoin = root.join("genres");
                predicates.add(genresJoin.in(request.getGenres()));
            }

            if (request.getDirector() != null && !request.getDirector().isEmpty()) {
                // Assuming director is a collection in the entity
                Join<MovieEntity, String> directorsJoin = root.join("director");
                predicates.add(directorsJoin.in(request.getDirector()));
            }

            if (request.getCast() != null && !request.getCast().isEmpty()) {
                // Assuming cast is a collection in the entity
                Join<MovieEntity, String> castJoin = root.join("cast");
                predicates.add(castJoin.in(request.getCast()));
            }

            if (request.getMinRating() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("imdbRating"),
                        request.getMinRating()
                ));
            }

            if (request.getMaxRating() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("imdbRating"),
                        request.getMaxRating()
                ));
            }

            if (request.getStartDate() != null) {
                predicates.add(cb.greaterThan(
                        root.get("releaseDate"),
                        request.getStartDate()
                ));
            }

            if (request.getEndDate() != null) {
                predicates.add(cb.lessThan(
                        root.get("releaseDate"),
                        request.getEndDate()
                ));
            }

            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }



    public SocketRequest getRecommendedFilm(MovieRecommendationRequest request) {
        SocketRequest socketRequest = new SocketRequest();
        List<MovieDto> movies = new ArrayList<>();
        for (Long id : request.getRecommendationsId()) {
            Optional<MovieEntity> movieEntity = this.movieRepository.findById(id);
            movieEntity.ifPresent(entity -> movies.add(MovieMapper.INSTANCE.toMovieDto(entity)));
        }
        socketRequest.setRecommendedMovies(movies);
        socketRequest.setRequestId(request.getRequestId());
        socketRequest.setFromUsername(request.getFromUsername());
        return socketRequest;
    }

    public Long getMovieCount() {
        return this.movieRepository.count();
    }

    public String fetchTrailerUrl(Long tmdbId) {
        String url = "https://api.themoviedb.org/3/movie/" + tmdbId + "/videos?api_key=" + apiKey;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
            if (results != null) {
                for (Map<String, Object> video : results) {
                    if ("YouTube".equals(video.get("site")) && "Trailer".equals(video.get("type"))) {
                        String key = (String) video.get("key");
                        return "https://www.youtube.com/watch?v=" + key;
                    }
                }
            }
        }

        return null;
    }
}
