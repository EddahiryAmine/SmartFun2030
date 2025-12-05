package com.ace.ticketservice.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("tickets")
public class TicketEntity {

    @Id
    private String id;

    private String matchId;
    private String userId;

    private TicketCategory category;
    private TicketStatus status;

    private int quantity;
    private double unitPrice;
    private double totalPrice;

    private LocalDateTime purchaseDate;
    private String qrCode;
}

