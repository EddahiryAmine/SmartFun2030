package com.ace.ticketservice.dto;


import com.ace.ticketservice.model.TicketCategory;
import com.ace.ticketservice.model.TicketStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResponse {

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
