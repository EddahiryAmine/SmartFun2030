package com.ace.ticketservice.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("favorites")
public class FavoriteEntity {

    @Id
    private String id;

    private String userId;
    private String matchId;

    private LocalDateTime createdAt;
}
