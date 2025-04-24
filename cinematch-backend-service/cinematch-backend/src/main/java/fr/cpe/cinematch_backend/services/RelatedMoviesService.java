package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.entities.MovieEntity;
import fr.cpe.cinematch_backend.entities.RelatedMoviesEntity;
import fr.cpe.cinematch_backend.exceptions.GenericNotFoundException;
import fr.cpe.cinematch_backend.mappers.MovieMapper;
import fr.cpe.cinematch_backend.repositories.MovieRepository;
import fr.cpe.cinematch_backend.repositories.RelatedMoviesRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatedMoviesService {

    @Autowired
    private RelatedMoviesRepository relatedMoviesRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieDto> getRelatedMoviesByMovie1(Long movieId) throws GenericNotFoundException {
        // Vérifie si le film existe
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new GenericNotFoundException(404, "Movie not found",
                        "Movie with ID " + movieId + " not found."));

        // Récupère toutes les relations où movie est movie1
        List<RelatedMoviesEntity> relations = relatedMoviesRepository.findAllByMovie1(movie);

        // Retourne la liste des movie2 associés, mappés en MovieDto
        return relations.stream()
                .map(RelatedMoviesEntity::getMovie2)
                .map(MovieMapper.INSTANCE::toMovieDto)
                .toList();
    }

}
