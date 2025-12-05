package com.ace.ticketservice.service;


import com.ace.ticketservice.client.MatchStadiumClient;
import com.ace.ticketservice.dto.MatchDto;
import com.ace.ticketservice.dto.PurchaseTicketRequest;
import com.ace.ticketservice.dto.StadiumDto;
import com.ace.ticketservice.dto.TicketResponse;
import com.ace.ticketservice.model.TicketCategory;
import com.ace.ticketservice.model.TicketEntity;
import com.ace.ticketservice.model.TicketStatus;
import com.ace.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final MatchStadiumClient matchStadiumClient;


    public int getAvailableSeatsForMatch(String matchId) {

        MatchDto match = matchStadiumClient.getMatchById(matchId);

        StadiumDto stadium = matchStadiumClient.getStadiumById(match.getStadiumId());
        int capacity = stadium.getCapacity();

        List<TicketEntity> tickets = ticketRepository.findByMatchId(matchId);

        int usedSeats = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.PAID || t.getStatus() == TicketStatus.RESERVED)
                .mapToInt(TicketEntity::getQuantity)
                .sum();

        return capacity - usedSeats;
    }


    public TicketResponse purchaseTicket(String userId, PurchaseTicketRequest request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("La quantité doit être > 0");
        }

        int alreadyOwned = getUserTotalTickets(userId);
        if (alreadyOwned + request.getQuantity() > 5) {
            throw new IllegalStateException(
                    "Vous ne pouvez pas acheter plus de 5 billets au total avec ce compte."
            );
        }

        int available = getAvailableSeatsForMatch(request.getMatchId());
        if (request.getQuantity() > available) {
            throw new IllegalStateException("Pas assez de places disponibles pour ce match");
        }

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("La quantité doit être > 0");
        }



        double unitPrice = getUnitPrice(request.getCategory());
        double totalPrice = unitPrice * request.getQuantity();

        TicketEntity ticket = new TicketEntity();
        ticket.setUserId(userId);
        ticket.setMatchId(request.getMatchId());
        ticket.setCategory(request.getCategory());
        ticket.setStatus(TicketStatus.PAID);
        ticket.setQuantity(request.getQuantity());
        ticket.setUnitPrice(unitPrice);
        ticket.setTotalPrice(totalPrice);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setQrCode(UUID.randomUUID().toString());

        TicketEntity saved = ticketRepository.save(ticket);

        // TODO: ici plus tard -> envoyer un event RabbitMQ pour PDF + mail

        return mapToResponse(saved);
    }



    private int getUserTotalTickets(String userId) {
        return ticketRepository.findByUserId(userId).stream()
                .filter(t -> t.getStatus() == TicketStatus.PAID || t.getStatus() == TicketStatus.RESERVED)
                .mapToInt(TicketEntity::getQuantity)
                .sum();
    }


    public List<TicketResponse> getTicketsByUser(String userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public double getUnitPrice(TicketCategory category) {
        return switch (category) {
            case NORMAL -> 100.0;
            case VIP -> 200.0;
            case VVIP -> 300.0;
        };
    }

    private TicketResponse mapToResponse(TicketEntity t) {
        TicketResponse r = new TicketResponse();
        r.setId(t.getId());
        r.setMatchId(t.getMatchId());
        r.setUserId(t.getUserId());
        r.setCategory(t.getCategory());
        r.setStatus(t.getStatus());
        r.setQuantity(t.getQuantity());
        r.setUnitPrice(t.getUnitPrice());
        r.setTotalPrice(t.getTotalPrice());
        r.setPurchaseDate(t.getPurchaseDate());
        r.setQrCode(t.getQrCode());
        return r;
    }



}

