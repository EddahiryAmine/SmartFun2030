package com.ace.authservice;


import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MongoCheckRunner implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(" Connected to MongoDB database: " + mongoTemplate.getDb().getName());
        System.out.println(" Collections: " + mongoTemplate.getDb().listCollectionNames().into(new java.util.ArrayList<>()));
    }
}

