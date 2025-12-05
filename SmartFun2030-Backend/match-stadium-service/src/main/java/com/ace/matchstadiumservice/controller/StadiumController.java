package com.ace.matchstadiumservice.controller;


import com.ace.matchstadiumservice.model.Stadium;
import com.ace.matchstadiumservice.service.StadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stadiums")
@RequiredArgsConstructor
public class StadiumController {

    private final StadiumService stadiumService;

    @GetMapping
    public List<Stadium> getStadiums(
            @RequestParam String country,
            @RequestParam(required = false) String city) {

        if (city != null && !city.isBlank()) {
            return stadiumService.getStadiumsByCountryAndCity(country, city);
        }
        return stadiumService.getStadiumsByCountry(country);
    }
    @GetMapping("/all")
    public List<Stadium> getAllStadiums() {
        return stadiumService.getAllStadiums();
    }

    @GetMapping("/{id}")
    public Stadium getStadiumById(@PathVariable String id) {
        return stadiumService.getStadiumById(id);
    }

    @PostMapping
    public Stadium createStadium(@RequestBody Stadium stadium) {
        return stadiumService.createStadium(stadium);
    }

    @PutMapping("/{id}")
    public Stadium updateStadium(@PathVariable String id, @RequestBody Stadium stadium) {
        return stadiumService.updateStadium(id, stadium);
    }

    @DeleteMapping("/{id}")
    public void deleteStadium(@PathVariable String id) {
        stadiumService.deleteStadium(id);
    }
}
