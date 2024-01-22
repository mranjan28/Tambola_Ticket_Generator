package com.test.tambolaticketgenerator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@Table(name = "tambola_ticket_values")
public class TicketNodeValue {

    @ManyToOne
    @JoinColumn(name = "ticket_ticket_id")
    private TicketNodeEntity ticketNodeEntity;
    private int rowNumber;
    private int columnNumber;
    private int cellValue;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public TicketNodeValue(int row, int column, int value, TicketNodeEntity ticketNodeEntity) {
        this.rowNumber = row;
        this.columnNumber = column;
        this.cellValue = value;
        this.ticketNodeEntity = ticketNodeEntity;
    }
}
