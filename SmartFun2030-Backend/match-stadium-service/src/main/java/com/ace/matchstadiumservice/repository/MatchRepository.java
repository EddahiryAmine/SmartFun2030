package com.ace.matchstadiumservice.repository;


import com.ace.matchstadiumservice.model.MatchEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MatchRepository extends MongoRepository<MatchEntity, String> {

    List<MatchEntity> findByStadiumId(String stadiumId);

}
