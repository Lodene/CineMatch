package fr.cpe.cinematch_backend.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MovieSearchRequest {
    private String title;
    private List<String> genres;
    private List<String> director;
    private List<String> cast;
    private Float minRating;
    private Float maxRating;
    private Date startDate;
    private Date endDate;
}
