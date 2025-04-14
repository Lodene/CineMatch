package fr.cpe.cinematch_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "movie")
public class MovieEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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

    @ElementCollection
    private List<String> genres;


    @ElementCollection
    private List<String> productionsCompanies;


    @ElementCollection
    private List<String> productionsCountries;

    @ElementCollection
    private List<String> spokenLanguages;

    @ElementCollection
    private List<String> cast;

    @ElementCollection
    private List<String> director;

    @ElementCollection
    private List<String> directorOfPhotograpy;

    @ElementCollection
    private List<String> writers;

    @ElementCollection
    private List<String> producers;
    @ElementCollection
    private List<String> musicComposer;
    private float imdbRating;
    private float imdbVotes;
    private String posterPath;
    private String backdropPath;

}
