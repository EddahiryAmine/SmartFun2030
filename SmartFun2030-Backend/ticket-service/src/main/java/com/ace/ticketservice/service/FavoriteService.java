package com.ace.ticketservice.service;


import com.ace.ticketservice.model.FavoriteEntity;
import com.ace.ticketservice.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public List<FavoriteEntity> getFavoritesForUser(String userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public FavoriteEntity addFavorite(String userId, String matchId) {
        return favoriteRepository.findByUserIdAndMatchId(userId, matchId)
                .orElseGet(() -> {
                    FavoriteEntity fav = new FavoriteEntity();
                    fav.setUserId(userId);
                    fav.setMatchId(matchId);
                    fav.setCreatedAt(LocalDateTime.now());
                    return favoriteRepository.save(fav);
                });
    }

    public void removeFavorite(String userId, String matchId) {
        favoriteRepository.deleteByUserIdAndMatchId(userId, matchId);
    }
}
