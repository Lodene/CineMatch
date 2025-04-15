package fr.cpe.cinematch_backend.mappers;

import fr.cpe.cinematch_backend.dtos.ReviewDto;
import fr.cpe.cinematch_backend.entities.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(target = "idMovie", expression = "java(reviewEntity.getMovie().getId())")
    public ReviewDto toReviewDto(ReviewEntity reviewEntity);

    public ReviewEntity toReviewEntity(ReviewDto dto);
}