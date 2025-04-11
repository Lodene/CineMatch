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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LovedMovieService {

    @Autowired
    private LovedMovieRepository lovedMovieRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MovieRepository movieRepository;

    public void likeMovie(LovedMovieRequestDto dto, String username) throws GenericNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with username '" + username + "' couldn't be found"));
        MovieEntity movie = movieRepository.findById(dto.getIdMovie())
                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                        "Movie with id '" + dto.getIdMovie() + "' couldn't be found"));

        if (lovedMovieRepository.existsByUserAndMovie(user, movie))
            return;

        LovedMovieEntity lovedMovie = new LovedMovieEntity();
        lovedMovie.setUser(user);
        lovedMovie.setMovie(movie);
        lovedMovieRepository.save(lovedMovie);
    }

    public void unlikeMovie(Long idMovie, String username) throws GenericNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with username '" + username + "' couldn't be found"));
        MovieEntity movie = movieRepository.findById(idMovie)
                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                        "Movie with id '" + idMovie + "' couldn't be found"));

        lovedMovieRepository.findByUserAndMovie(user, movie)
                .ifPresent(lovedMovieRepository::delete);
    }

    public List<MovieDto> getLovedMovies(String username) throws GenericNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with username '" + username + "' couldn't be found"));

        return lovedMovieRepository.findByUser(user).stream()
                .map(lovedMovie -> MovieMapper.INSTANCE.toMovieDto(lovedMovie.getMovie()))
                .toList();
    }

    public List<MovieDto> getLovedMoviesByUserId(Long idUser) throws GenericNotFoundException {
        AppUser user = appUserRepository.findById(idUser)
                .orElseThrow(() -> new GenericNotFoundException(404, "User not found",
                        "User with id '" + idUser + "' couldn't be found"));

        return lovedMovieRepository.findByUser(user).stream()
                .map(lm -> MovieMapper.INSTANCE.toMovieDto(lm.getMovie()))
                .toList();
    }

}
