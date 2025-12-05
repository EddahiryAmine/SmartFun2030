package com.ace.ticketservice.dto;


import lombok.Data;

@Data
public class MatchDto {
    private String id;
    private String homeTeam;
    private String awayTeam;

    private String kickoff;
    private String status;

    private String stadiumId;

    private String phase;
    private String groupName;
}

