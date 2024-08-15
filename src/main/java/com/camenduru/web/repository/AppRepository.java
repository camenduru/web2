package com.camenduru.web.repository;

import com.camenduru.web.domain.App;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the App entity.
 */
@Repository
public interface AppRepository extends MongoRepository<App, String> {
    @Query("{'type': ?0}")
    Optional<App> findOneByType(String type);
}
