package fr.cpe.cinematch_backend.dtos.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {

    private Long idMovie;

    @Min(0)
    @Max(10)
    private Integer note;

    private String description;
}
