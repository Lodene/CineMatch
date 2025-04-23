package cpe.cinematch_backend.orchestrator.repositories;

import cpe.cinematch_backend.orchestrator.entities.SimilarMovieRequestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface SimilarMovieRequestRepository extends MongoRepository<SimilarMovieRequestEntity,String>
{
	Optional<SimilarMovieRequestEntity> findById(UUID cardId);
}
