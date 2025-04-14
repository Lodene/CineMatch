package fr.cpe.cinematch_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Long id;
    private String title;
    private float voteAverage;
    private float voteCount;
    private String status;
    private Date releaseDate;
    private float revenue;
    private int runtime;
    private int budget;
    private String imdbId;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private float popularity;
    private List<String> genres;
    private List<String> productionsCompanies;
    private List<String> productionsCountries;
    private List<String> spokenLanguages;
    private List<String> cast;
    private List<String> director;
    private List<String> directorOfPhotograpy;
    private List<String> writers;
    private List<String> producers;
    private List<String> musicComposer;
    private float imdb_rating;
    private float imdb_votes;
    private String posterPath;
    private String backdropPath;
}
