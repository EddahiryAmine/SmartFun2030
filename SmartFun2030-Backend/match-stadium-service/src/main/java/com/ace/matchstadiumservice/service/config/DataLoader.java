package com.ace.matchstadiumservice.service.config;


import com.ace.matchstadiumservice.model.MatchEntity;
import com.ace.matchstadiumservice.model.MatchImport;
import com.ace.matchstadiumservice.model.MatchPhase;
import com.ace.matchstadiumservice.model.Stadium;
import com.ace.matchstadiumservice.repository.MatchRepository;
import com.ace.matchstadiumservice.repository.StadiumRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final StadiumRepository stadiumRepository;
    private final MatchRepository matchRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {

        if (stadiumRepository.count() == 0) {
            System.out.println("🔄 Importing stadiums.json ...");
            InputStream input = new ClassPathResource("data/stadiums.json").getInputStream();
            List<Stadium> stadiums = objectMapper.readValue(input, new TypeReference<List<Stadium>>(){});
            stadiumRepository.saveAll(stadiums);
            System.out.println("✅ Stadiums imported.");

        }

        if (matchRepository.count() == 0) {
            System.out.println("🔄 Importing matches.json ...");

            InputStream input = new ClassPathResource("data/matches.json").getInputStream();
            List<MatchImport> importedMatches =
                    objectMapper.readValue(input, new TypeReference<List<MatchImport>>() {});

            for (MatchImport m : importedMatches) {

                Stadium stadium = stadiumRepository.findByName(m.getStadiumName());
                if (stadium == null) {
                    System.out.println(" Stadium not found: " + m.getStadiumName());
                    continue;
                }

                MatchEntity match = new MatchEntity();
                match.setHomeTeam(m.getHomeTeam());
                match.setAwayTeam(m.getAwayTeam());
                match.setKickoff(LocalDateTime.parse(m.getKickoff()));
                match.setStatus(m.getStatus());
                match.setStadiumId(stadium.getId());
                if (m.getPhase() != null && !m.getPhase().isBlank()) {
                    try {
                        match.setPhase(MatchPhase.valueOf(m.getPhase()));
                    } catch (IllegalArgumentException ex) {
                        System.out.println("⚠️ Unknown phase: " + m.getPhase()
                                + " for match " + m.getHomeTeam() + " vs " + m.getAwayTeam());
                        match.setPhase(MatchPhase.GROUP); // valeur par défaut
                    }
                } else {
                    match.setPhase(MatchPhase.GROUP);
                }

                match.setGroupName(m.getGroupName());

                matchRepository.save(match);
            }

            System.out.println(" Matches imported.");
        }
    }
}

