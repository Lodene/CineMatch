package fr.cpe.cinematch_backend.dtos.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TmdbMovie {
    @JsonProperty("aspect_ratio")
    private double aspectRatio;

    private int height;

    @JsonProperty("iso_639_1")
    private String languageCode;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    private int width;
}
