package com.ace.matchstadiumservice.controller;



import com.ace.matchstadiumservice.model.MatchEntity;
import com.ace.matchstadiumservice.model.Stadium;
import com.ace.matchstadiumservice.service.MatchService;
import com.ace.matchstadiumservice.service.StadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/stadium/{stadiumId}")
    public List<MatchEntity> getMatchesByStadium(@PathVariable String stadiumId) {
        return matchService.getMatchesByStadium(stadiumId);
    }
    @GetMapping("/all")
    public List<MatchEntity>getAllMatches() {
        return matchService.getAllMatches();
    }
    @GetMapping("/{id}")
    public MatchEntity getMatchById(@PathVariable String id) {
        return matchService.getMatchById(id);
    }

    // ----------- ADMIN -----------
    @PostMapping
    public MatchEntity createMatch(@RequestBody MatchEntity match) {
        return matchService.createMatch(match);
    }


    @PutMapping("/{id}")
    public MatchEntity updateMatch(@PathVariable String id, @RequestBody MatchEntity match) {
        return matchService.updateMatch(id, match);
    }

    @DeleteMapping("/{id}")
    public void deleteMatch(@PathVariable String id) {
        matchService.deleteMatch(id);
    }



}

