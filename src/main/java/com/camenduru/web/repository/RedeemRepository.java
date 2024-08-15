package com.camenduru.web.repository;

import com.camenduru.web.domain.Redeem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Redeem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RedeemRepository extends MongoRepository<Redeem, String> {}
