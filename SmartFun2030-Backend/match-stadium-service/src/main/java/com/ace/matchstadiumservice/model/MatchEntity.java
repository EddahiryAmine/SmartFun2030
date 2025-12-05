package com.ace.matchstadiumservice.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("matches")
public class MatchEntity {

    @Id
    private String id;

    private String homeTeam;
    private String awayTeam;

    private LocalDateTime kickoff;
    private String status; // "SCHEDULED", "FINISHED", etc.

    private String stadiumId;
    private MatchPhase phase;   // GROUP, QUARTER_FINAL, etc.
    private String groupName; // référence vers Stadium
}
