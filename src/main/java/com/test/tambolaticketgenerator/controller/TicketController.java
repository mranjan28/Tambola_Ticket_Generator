package com.test.tambolaticketgenerator.controller;

import com.test.tambolaticketgenerator.model.TicketNode;
import com.test.tambolaticketgenerator.service.TambolaTicketGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TicketController {
    private final TambolaTicketGeneratorService tambolaTicketGeneratorService;

    @Autowired
    public TicketController(TambolaTicketGeneratorService ticketGenerator) {
        this.tambolaTicketGeneratorService = ticketGenerator;
    }

    @PostMapping("/generate-tickets")
    public ResponseEntity generateTickets(@RequestParam("noOfSet") int noOfSet) {
        if (noOfSet <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Map<Integer, Map<Integer, String>> nodes = tambolaTicketGeneratorService.generateTickets(noOfSet);

        return new ResponseEntity<>(nodes, HttpStatus.OK);
    }

    @GetMapping("/getAllTickets")
    public ResponseEntity getAllTickets() {
        Map<Integer, String> tickets = tambolaTicketGeneratorService.getAllTickets();

        return new ResponseEntity(tickets, HttpStatus.OK);
    }
}
