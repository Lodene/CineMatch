package fr.cpe.cinematch_backend.dtos.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TmdbResponse implements Serializable {
    private long id;
    @JsonProperty("posters")
    private List<TmdbMovie> posters;
    @JsonProperty("backdrops")
    private List<TmdbMovie> backdrops;
}

