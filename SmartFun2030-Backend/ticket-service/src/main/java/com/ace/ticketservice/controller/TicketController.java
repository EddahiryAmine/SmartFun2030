package com.ace.ticketservice.controller;


import com.ace.ticketservice.dto.AddToCartRequest;
import com.ace.ticketservice.dto.PurchaseTicketRequest;
import com.ace.ticketservice.dto.TicketResponse;
import com.ace.ticketservice.model.CartItemEntity;
import com.ace.ticketservice.model.FavoriteEntity;
import com.ace.ticketservice.service.CartService;
import com.ace.ticketservice.service.FavoriteService;
import com.ace.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final FavoriteService favoriteService;
    private final CartService cartService;

    @PostMapping("/purchase")
    public ResponseEntity<TicketResponse> purchase(
            @RequestBody PurchaseTicketRequest request,
            @RequestHeader("X-User-Id") String userId
    ) {
        TicketResponse response = ticketService.purchaseTicket(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<TicketResponse>> myTickets(
            @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }

    @GetMapping("/availability/{matchId}")
    public ResponseEntity<Integer> getAvailability(@PathVariable String matchId) {
        return ResponseEntity.ok(ticketService.getAvailableSeatsForMatch(matchId));
    }
    @PostMapping("/favorites/{matchId}")
    public ResponseEntity<Void> addFavorite(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String matchId
    ) {
        favoriteService.addFavorite(userId, matchId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteEntity>> getFavorites(
            @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(favoriteService.getFavoritesForUser(userId));
    }

    @DeleteMapping("/favorites/{matchId}")
    public ResponseEntity<Void> removeFavorite(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String matchId
    ) {
        favoriteService.removeFavorite(userId, matchId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/cart")
    public ResponseEntity<List<CartItemEntity>> getCart(
            @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(cartService.getCartForUser(userId));
    }

    @PostMapping("/cart")
    public ResponseEntity<CartItemEntity> addToCart(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody AddToCartRequest req
    ) {
        return ResponseEntity.ok(cartService.addToCart(userId, req));
    }

    @DeleteMapping("/cart/{itemId}")
    public ResponseEntity<Void> removeCartItem(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String itemId
    ) {
        cartService.removeCartItem(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Void> clearCart(
            @RequestHeader("X-User-Id") String userId
    ) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

}

