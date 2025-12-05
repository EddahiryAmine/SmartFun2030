package com.ace.matchstadiumservice.repository;


import com.ace.matchstadiumservice.model.Stadium;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StadiumRepository extends MongoRepository<Stadium, String> {

    List<Stadium> findByCountryIgnoreCase(String country);

    List<Stadium> findByCountryIgnoreCaseAndCityIgnoreCase(String country, String city);
    Stadium findByName(String name);

}
