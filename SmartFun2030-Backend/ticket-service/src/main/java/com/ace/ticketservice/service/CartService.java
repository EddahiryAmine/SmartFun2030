package com.ace.ticketservice.service;


import com.ace.ticketservice.dto.AddToCartRequest;
import com.ace.ticketservice.model.CartItemEntity;
import com.ace.ticketservice.model.TicketCategory;
import com.ace.ticketservice.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository; // 👈 important


    public List<CartItemEntity> getCartForUser(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public CartItemEntity addToCart(String userId, AddToCartRequest req) {
        // tu peux simplifier au début : 1 item = 1 ligne
        CartItemEntity item = new CartItemEntity();
        item.setUserId(userId);
        item.setMatchId(req.getMatchId());
        item.setCategory(req.getCategory());
        item.setQuantity(req.getQuantity());
        item.setUnitPrice(getUnitPrice(req.getCategory())); // tu l'as déjà
        return cartItemRepository.save(item);
    }

    private double getUnitPrice(TicketCategory category) {
        return switch (category) {
            case NORMAL -> 100.0;
            case VIP -> 200.0;
            case VVIP -> 300.0;
        };
    }


    public void removeCartItem(String userId, String itemId) {
        cartItemRepository.findById(itemId).ifPresent(item -> {
            if (item.getUserId().equals(userId)) {
                cartItemRepository.delete(item);
            }
        });
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}

