package com.camenduru.web.repository;

import com.camenduru.web.domain.App;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the App entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppRepository extends MongoRepository<App, String> {}
