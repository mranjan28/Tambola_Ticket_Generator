package com.test.tambolaticketgenerator.model;

import java.util.Arrays;

import static com.test.tambolaticketgenerator.constants.TambolaConstants.NUM_COLS;
import static com.test.tambolaticketgenerator.constants.TambolaConstants.NUM_ROWS;

public class TicketNode {
    private int[][] values;

    public TicketNode() {
        this.values = new int[NUM_ROWS][NUM_COLS];
        for (int r = 0; r < NUM_ROWS; r++) {
            for (int c = 0; c < NUM_COLS; c++) {
                values[r][c] = 0;
            }
        }
    }

    public int[][] getValues() {
        return values;
    }

    public void setValues(int[][] values) {
        this.values = values;
    }

    public int getRowCount(int r) {
        return (int) Arrays.stream(values[r]).filter(val -> val != 0).count();
    }

    public int getColCount(int c) {
        return (int) Arrays.stream(values).filter(row -> row[c] != 0).count();
    }

    public int getCellValue(int r, int c) {
        return values[r][c];
    }

    public void setCellValue(int r, int c, int value) {
        values[r][c] = value;
    }

    private void sortColumnWithThreeNumbers(int c) {
        int[] tempArr = {values[0][c], values[1][c], values[2][c]};
        Arrays.sort(tempArr);

        for (int r = 0; r < NUM_ROWS; r++) {
            values[r][c] = tempArr[r];
        }
    }

    private void sortColumnWithTwoNumbers(int c) {
        int emptyCell = getEmptyCellInCol(c);
        if (emptyCell == -1) {
            throw new IllegalStateException("Invalid function called. Column has 3 cells filled.");
        }

        int cell1 = (emptyCell == 0) ? 1 : 0;
        int cell2 = (emptyCell == 2) ? 1 : 2;

        if (values[cell1][c] > values[cell2][c]) {
            int temp = values[cell1][c];
            values[cell1][c] = values[cell2][c];
            values[cell2][c] = temp;
        }
    }

    private void sortColumn(int c) {
        if (getColCount(c) == 1) {
            // Do nothing, only one element in the column
        } else if (getColCount(c) == 2) {
            sortColumnWithTwoNumbers(c);
        } else {
            sortColumnWithThreeNumbers(c);
        }
    }

    public void sortColumns() {
        for (int c = 0; c < NUM_COLS; c++) {
            sortColumn(c);
        }
    }

    public int getEmptyCellInCol(int c) {
        for (int r = 0; r < NUM_ROWS; r++) {
            if (values[r][c] == 0) {
                return r;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : values) {
            sb.append("[");
            for (int value : row) {
                sb.append(value).append(", ");
            }
            if (row.length > 0) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("], ");
        }
        if (values.length > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }
}
