package com.camenduru.web.repository;

import com.camenduru.web.domain.Setting;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Setting entity.
 */
@Repository
public interface SettingRepository extends MongoRepository<Setting, String> {
    @Query("{'login': ?0}")
    Page<Setting> findAllByUserIsCurrentUser(Pageable pageable, String login);

    @Query("{'login': ?0}")
    Optional<Setting> findAllByUserIsCurrentUser(String login);

    @Query("{}")
    Page<Setting> findAll(Pageable pageable);

    @Query("{}")
    List<Setting> findAll();

    @Query("{'id': ?0, 'login': ?1}")
    Optional<Setting> findOneByUserIsCurrentUser(String id, String login);

    @Query("{'login': ?0}")
    Optional<Setting> findOneByLogin(String login);
}
