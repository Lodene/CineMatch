package fr.cpe.cinematch_backend.mappers;

import fr.cpe.cinematch_backend.dtos.review.ReviewDto;
import fr.cpe.cinematch_backend.entities.review.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);
    public ReviewDto toReviewDto(ReviewEntity reviewEntity);
    public ReviewEntity toReviewEntity(ReviewDto dto);
}
