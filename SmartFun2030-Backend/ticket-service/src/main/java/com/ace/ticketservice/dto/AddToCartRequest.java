package com.ace.ticketservice.dto;


import com.ace.ticketservice.model.TicketCategory;
import lombok.Data;

@Data
public class AddToCartRequest {
    private String matchId;
    private TicketCategory category;
    private int quantity;
}

