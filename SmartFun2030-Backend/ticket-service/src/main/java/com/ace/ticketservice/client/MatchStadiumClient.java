package com.ace.ticketservice.client;


import com.ace.ticketservice.dto.MatchDto;
import com.ace.ticketservice.dto.StadiumDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "match-stadium-service")
public interface MatchStadiumClient {

    @GetMapping("/api/matches/{id}")
    MatchDto getMatchById(@PathVariable("id") String id);

    @GetMapping("/api/stadiums/{id}")
    StadiumDto getStadiumById(@PathVariable("id") String id);
}

