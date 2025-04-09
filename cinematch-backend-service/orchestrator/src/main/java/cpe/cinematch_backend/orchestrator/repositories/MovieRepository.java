package cpe.cinematch_backend.orchestrator.repositories;

import cpe.cinematch_backend.orchestrator.entities.MovieEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface MovieRepository extends MongoRepository<MovieEntity,String>
{
	Optional<MovieEntity> findByMovieId(UUID cardId);
}
