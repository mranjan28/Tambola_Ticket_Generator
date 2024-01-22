package com.test.tambolaticketgenerator.generator;

import com.test.tambolaticketgenerator.model.TicketNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.test.tambolaticketgenerator.constants.TambolaConstants.*;

@Component
public class TicketGenerator {

    private static int getRandomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    private static List<List<Integer>> initializeColumns() {
        List<List<Integer>> columns = new ArrayList<>();
        for (int i = 0; i < NUM_COLS; i++) {
            columns.add(new ArrayList<>());
            for (int j = 1; j <= 10; j++) {
                columns.get(i).add(i * 10 + j);
            }
        }
        return columns;
    }

    private static void initializeElements(List<List<Integer>> columns, List<List<List<Integer>>> sets) {
        for (int i = 0; i < NUM_COLS; i++) {
            for (int j = 0; j < NUM_SETS; j++) {
                int randNumIndex = getRandomNumber(0, columns.get(i).size() - 1);
                int randNum = columns.get(i).get(randNumIndex);
                columns.get(i).remove(randNumIndex);

                List<Integer> set = sets.get(j).get(i);
                set.add(randNum);
            }
        }
    }

    private static void distributeRemainingElements(List<List<Integer>> columns, List<List<List<Integer>>> sets) {
        for (int pass = 0; pass < 4; pass++) {
            for (int i = 0; i < NUM_COLS; i++) {
                List<Integer> col = columns.get(i);
                if (col.isEmpty()) continue;

                int randNumIndex = getRandomNumber(0, col.size() - 1);
                int randNum = col.get(randNumIndex);

                boolean vacantSetFound = false;
                while (!vacantSetFound) {
                    int randSetIndex = getRandomNumber(0, NUM_SETS - 1);
                    List<List<Integer>> randSet = sets.get(randSetIndex);

                    if (getNumberOfElementsInSet(randSet) == NUM_ELEMENTS_IN_SET || randSet.get(i).size() == 3)
                        continue;

                    vacantSetFound = true;
                    randSet.get(i).add(randNum);

                    col.remove(randNumIndex);
                }
            }
        }
    }

    private static int getNumberOfElementsInSet(List<List<Integer>> set) {
        return set.stream().mapToInt(List::size).sum();
    }

    private static void arrangeTickets(List<List<List<Integer>>> sets, List<TicketNode> nodeList) {
        for (int setIndex = 0; setIndex < NUM_SETS; setIndex++) {
            List<List<Integer>> currSet = sets.get(setIndex);
            TicketNode currTicket = nodeList.get(setIndex);

            for (int r = 0; r < NUM_ROWS; r++) {
                for (int size = 3; size > 0; size--) {
                    if (currTicket.getRowCount(r) == 5) break;
                    for (int colIndex = 0; colIndex < NUM_COLS; colIndex++) {
                        if (currTicket.getRowCount(r) == 5) break;
                        if (currTicket.getCellValue(r, colIndex) != 0) continue;

                        List<Integer> currSetCol = currSet.get(colIndex);
                        if (currSetCol.size() != size) continue;

                        currTicket.setCellValue(r, colIndex, currSetCol.remove(0));
                    }
                }
            }
        }
    }

    private void sortInternalSets(List<List<List<Integer>>> sets) {
        for (List<List<Integer>> set : sets) {
            for (int i = 0; i < NUM_COLS; i++) {
                if (set.get(i).isEmpty()) {
                    int randColIndex = getRandomNumber(0, NUM_COLS - 1);
                    while (set.get(randColIndex).size() <= 1) {
                        randColIndex = getRandomNumber(0, NUM_COLS - 1);
                    }

                    int randNumIndex = getRandomNumber(0, set.get(randColIndex).size() - 1);
                    int randNum = set.get(randColIndex).remove(randNumIndex);

                    set.get(i).add(randNum);
                }
            }
        }
        sets.forEach(set -> set.forEach(Collections::sort));
    }

    public List<TicketNode> generateTickets() {
        List<TicketNode> nodeList = new ArrayList<>();
        for (int i = 0; i < NUM_SETS; i++) {
            nodeList.add(new TicketNode());
        }

        List<List<Integer>> columns = initializeColumns();

        List<List<List<Integer>>> sets = new ArrayList<>();
        for (int i = 0; i < NUM_SETS; i++) {
            sets.add(new ArrayList<>());
            for (int j = 0; j < NUM_COLS; j++) {
                sets.get(i).add(new ArrayList<>());
            }
        }

        initializeElements(columns, sets);

        List<Integer> lastCol = columns.get(NUM_COLS - 1);
        int randNumIndex = getRandomNumber(0, lastCol.size() - 1);
        int randNum = lastCol.get(randNumIndex);

        int randSetIndex = getRandomNumber(0, NUM_SETS - 1);
        List<Integer> randSet = sets.get(randSetIndex).get(NUM_COLS - 1);
        randSet.add(randNum);

        lastCol.remove(randNumIndex);

        distributeRemainingElements(columns, sets);

        sortInternalSets(sets);

        arrangeTickets(sets, nodeList);

        try {
            for (int i = 0; i < NUM_SETS; i++) {
                nodeList.get(i).sortColumns();
            }
        } catch (Exception e) {
            System.out.println("Note: There is a small probability that columns may not be sorted. " + "It should not impact the gameplay.");
            System.out.println("Please create an issue in the GitHub repository for investigation.");
            System.out.println(e.getMessage());
        }

        return nodeList;
    }
}