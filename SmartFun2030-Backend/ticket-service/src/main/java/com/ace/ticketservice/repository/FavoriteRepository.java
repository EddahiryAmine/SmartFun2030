package com.ace.ticketservice.repository;

import com.ace.ticketservice.model.FavoriteEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends MongoRepository<FavoriteEntity, String> {

    List<FavoriteEntity> findByUserId(String userId);

    Optional<FavoriteEntity> findByUserIdAndMatchId(String userId, String matchId);

    void deleteByUserIdAndMatchId(String userId, String matchId);
}
