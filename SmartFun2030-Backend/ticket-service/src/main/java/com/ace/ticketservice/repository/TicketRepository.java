package com.ace.ticketservice.repository;

import com.ace.ticketservice.model.FavoriteEntity;
import com.ace.ticketservice.model.TicketEntity;
import com.ace.ticketservice.model.TicketStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends MongoRepository<TicketEntity, String> {

    List<TicketEntity> findByMatchId(String matchId);

    List<TicketEntity> findByUserId(String userId);

}

