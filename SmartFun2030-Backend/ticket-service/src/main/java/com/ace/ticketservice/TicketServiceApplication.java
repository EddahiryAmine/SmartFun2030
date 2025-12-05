package com.ace.ticketservice;

import com.ace.ticketservice.model.TicketCategory;
import com.ace.ticketservice.model.TicketEntity;
import com.ace.ticketservice.model.TicketStatus;
import com.ace.ticketservice.repository.TicketRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class TicketServiceApplication {
    @Bean
    public CommandLineRunner testTicket(TicketRepository ticketRepository) {
        return args -> {
            if (ticketRepository.count() == 0) {
                TicketEntity t = new TicketEntity();
                t.setMatchId("TEST_MATCH");
                t.setUserId("TEST_USER");
                t.setCategory(TicketCategory.NORMAL);
                t.setStatus(TicketStatus.PAID);
                t.setQuantity(1);
                t.setUnitPrice(100.0);
                t.setTotalPrice(100.0);
                t.setPurchaseDate(LocalDateTime.now());
                t.setQrCode(UUID.randomUUID().toString());

                ticketRepository.save(t);
                System.out.println("✅ Ticket de test inséré dans Mongo");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(TicketServiceApplication.class, args);
    }

}
