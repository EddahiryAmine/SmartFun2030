package com.ace.ticketservice.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketTestController {

    @GetMapping("/ping")
    public String ping() {
        return "ticket-service OK";
    }
}
