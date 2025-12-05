package com.ace.matchstadiumservice.service;



import com.ace.matchstadiumservice.model.MatchEntity;
import com.ace.matchstadiumservice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public List<MatchEntity> getMatchesByStadium(String stadiumId) {
        return matchRepository.findByStadiumId(stadiumId);
    }

    public MatchEntity getMatchById(String id) {
        return matchRepository.findById(id).orElse(null);
    }
    public List<MatchEntity>getAllMatches (){
        return matchRepository.findAll();
    }


    // ---------- ADMIN ------------

    public MatchEntity createMatch(MatchEntity match) {
        match.setId(null); // laisser MongoDB générer l’ID
        return matchRepository.save(match);
    }

    public MatchEntity updateMatch(String id, MatchEntity updated) {
        MatchEntity existing = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        existing.setHomeTeam(updated.getHomeTeam());
        existing.setAwayTeam(updated.getAwayTeam());
        existing.setKickoff(updated.getKickoff());
        existing.setStatus(updated.getStatus());
        existing.setStadiumId(updated.getStadiumId());

        return matchRepository.save(existing);
    }

    public void deleteMatch(String id) {
        matchRepository.deleteById(id);
    }
}

