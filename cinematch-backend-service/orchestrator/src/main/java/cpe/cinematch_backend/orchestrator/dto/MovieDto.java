package cpe.cinematch_backend.orchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MovieDto
{
	private Long id;
	private String title;
	private float voteAverage;
	private float voteCount;
	private String status;
	private Date releaseDate;
	private float revenue;
	private float runtime;
	private float budget;
	private String imdbId;
	private String originalLanguage;
	private String originalTitle;
	private String overview;

	private float popularity;

	private List<String> genres;



	private List<String> productionCompanies;



	private List<String> productionCountries;

	private List<String> spokenLanguages;


	private List<String> cast;

	private List<String> director;


	private List<String> directorOfPhotography;


	private List<String> writers;

	private List<String> producers;

	private List<String> musicComposer;
	private float imdbRating;
	private float imdbVotes;
	private String posterPath;
	private String backdropPath;
}
