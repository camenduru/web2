package com.camenduru.web.repository;

import com.camenduru.web.domain.Setting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Setting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SettingRepository extends MongoRepository<Setting, String> {}
