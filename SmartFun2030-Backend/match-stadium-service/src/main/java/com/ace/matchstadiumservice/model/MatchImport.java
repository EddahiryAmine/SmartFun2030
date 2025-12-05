package com.ace.matchstadiumservice.model;


import lombok.Data;

@Data
public class MatchImport {
    private String homeTeam;
    private String awayTeam;
    private String kickoff;
    private String stadiumName;
    private String status;
    private String phase;
    private String groupName;
}

