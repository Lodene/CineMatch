package cpe.cinematch_backend.orchestrator.repositories;

import cpe.cinematch_backend.orchestrator.entities.MovieRecommendationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface MovieRecommendationRepository extends MongoRepository<MovieRecommendationEntity,String>
{
	Optional<MovieRecommendationEntity> findById(UUID cardId);
}
