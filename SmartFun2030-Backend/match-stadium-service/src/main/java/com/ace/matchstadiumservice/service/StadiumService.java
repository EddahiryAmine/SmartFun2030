package com.ace.matchstadiumservice.service;



import com.ace.matchstadiumservice.model.Stadium;
import com.ace.matchstadiumservice.repository.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StadiumService {

    private final StadiumRepository stadiumRepository;

    public List<Stadium> getStadiumsByCountry(String country) {
        return stadiumRepository.findByCountryIgnoreCase(country);
    }

    public List<Stadium> getStadiumsByCountryAndCity(String country, String city) {
        return stadiumRepository.findByCountryIgnoreCaseAndCityIgnoreCase(country, city);
    }

    public Stadium getStadiumById(String id) {
        return stadiumRepository.findById(id).orElse(null);
    }
    public Stadium createStadium(Stadium stadium) {
        stadium.setId(null); // Mongo va en générer un
        return stadiumRepository.save(stadium);
    }

    public Stadium updateStadium(String id, Stadium stadium) {
        Stadium existing = stadiumRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Stadium not found")
        );

        existing.setName(stadium.getName());
        existing.setCity(stadium.getCity());
        existing.setCountry(stadium.getCountry());
        existing.setCapacity(stadium.getCapacity());
        existing.setDescription(stadium.getDescription());
        existing.setLatitude(stadium.getLatitude());
        existing.setLongitude(stadium.getLongitude());
        existing.setImages(stadium.getImages());

        return stadiumRepository.save(existing);
    }

    public void deleteStadium(String id) {
        stadiumRepository.deleteById(id);
    }
    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }


}
