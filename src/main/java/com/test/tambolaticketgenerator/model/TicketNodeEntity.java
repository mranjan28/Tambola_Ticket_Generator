package com.test.tambolaticketgenerator.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.test.tambolaticketgenerator.constants.TambolaConstants.NUM_COLS;
import static com.test.tambolaticketgenerator.constants.TambolaConstants.NUM_ROWS;


@Data
@Entity
@Table(name = "tambola_tickets")
public class TicketNodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_ticket_id")
    private List<TicketNodeValue> values;

    public static TicketNodeEntity entityFromTicketNode(TicketNode ticketNode) {
        TicketNodeEntity ticketNodeEntity = new TicketNodeEntity();
        List<TicketNodeValue> valueEntities = new ArrayList<>();

        for (int i = 0; i < ticketNode.getValues().length; i++) {
            for (int j = 0; j < ticketNode.getValues()[i].length; j++) {
                TicketNodeValue value = new TicketNodeValue(i, j, ticketNode.getValues()[i][j], ticketNodeEntity);
                valueEntities.add(value);
            }
        }

        ticketNodeEntity.setValues(valueEntities);
        return ticketNodeEntity;
    }

    public static TicketNode convertToTicketNode(TicketNodeEntity ticketNodeEntity) {
        TicketNode ticketNode = new TicketNode();

        if (ticketNodeEntity != null && ticketNodeEntity.getValues() != null) {
            int[][] values = new int[NUM_ROWS][NUM_COLS];

            for (TicketNodeValue valueEntity : ticketNodeEntity.getValues()) {
                int row = valueEntity.getRowNumber();
                int col = valueEntity.getColumnNumber();
                int cellValue = valueEntity.getCellValue();

                values[row][col] = cellValue;
            }

            ticketNode.setValues(values);
        }

        return ticketNode;
    }
}
