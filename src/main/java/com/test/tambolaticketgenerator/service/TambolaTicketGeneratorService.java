package com.test.tambolaticketgenerator.service;

import com.test.tambolaticketgenerator.generator.TicketGenerator;
import com.test.tambolaticketgenerator.model.TicketNode;
import com.test.tambolaticketgenerator.model.TicketNodeEntity;
import com.test.tambolaticketgenerator.repository.TicketNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TambolaTicketGeneratorService {

    @Autowired
    private TicketGenerator ticketGenerator;

    @Autowired
    private TicketNodeRepository ticketNodeRepository;

    public Map<Integer, Map<Integer, String>> generateTickets(int numSets) {
        Map<Integer, Map<Integer, String>> ticketSetMap = new HashMap<>();
        List<TicketNode> allTickets = new ArrayList<>();

        for (int i = 1; i <= numSets; i++) {
            List<TicketNode> ticketNodes = ticketGenerator.generateTickets();
            allTickets.addAll(ticketNodes);

            Map<Integer, String> ticketMap = IntStream.range(0, ticketNodes.size()).boxed().collect(Collectors.toMap(j -> j + 1, j -> ticketNodes.get(j).toString()));

            ticketSetMap.put(i, ticketMap);
        }

        saveTicketNode(allTickets);
        return ticketSetMap;
    }

    public void saveTicketNode(List<TicketNode> ticketNode) {
        ticketNodeRepository.saveAll(ticketNode.stream().map(TicketNodeEntity::entityFromTicketNode).collect(Collectors.toList()));
    }

    public Map<Integer, String> getAllTickets() {
        List<TicketNodeEntity> entities = ticketNodeRepository.findAll();
        List<TicketNode> nodeList = entities.stream().map(TicketNodeEntity::convertToTicketNode).toList();
        Map<Integer, String> ticketMap = IntStream.range(0, nodeList.size()).boxed().collect(Collectors.toMap(j -> j + 1, j -> nodeList.get(j).toString()));
        return ticketMap;
    }
}
