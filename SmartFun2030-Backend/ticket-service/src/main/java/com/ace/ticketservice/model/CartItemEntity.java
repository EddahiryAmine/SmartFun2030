package com.ace.ticketservice.model;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("cart_items")
public class CartItemEntity {

    @Id
    private String id;

    private String userId;
    private String matchId;
    private TicketCategory category;
    private int quantity;
    private double unitPrice;
}

