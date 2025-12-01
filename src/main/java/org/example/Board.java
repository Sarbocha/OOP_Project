package org.example;

/**
 * Game logic for Tic-Tac-Toe board.
 */
public class Board {
    private final String[][] cells = new String[3][3];

    public Board() { reset(); }

    public void setCell(String mark, int[] pos) { cells[pos[0]][pos[1]] = mark; }

    public void reset() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                cells[i][j] = "";
    }

    public boolean isFull() {
        for (String[] row : cells)
            for (String cell : row)
                if (cell.isEmpty()) return false;
        return true;
    }

    /**
     * Returns winning line coordinates or null if no winner.
     */
    public int[][] checkWinnerLine() {
        // Rows
        for (int i = 0; i < 3; i++)
            if (!cells[i][0].isEmpty() && cells[i][0].equals(cells[i][1]) && cells[i][1].equals(cells[i][2]))
                return new int[][]{{i,0},{i,1},{i,2}};

        // Columns
        for (int i = 0; i < 3; i++)
            if (!cells[0][i].isEmpty() && cells[0][i].equals(cells[1][i]) && cells[1][i].equals(cells[2][i]))
                return new int[][]{{0,i},{1,i},{2,i}};

        // Diagonal TL-BR
        if (!cells[0][0].isEmpty() && cells[0][0].equals(cells[1][1]) && cells[1][1].equals(cells[2][2]))
            return new int[][]{{0,0},{1,1},{2,2}};

        // Diagonal TR-BL
        if (!cells[0][2].isEmpty() && cells[0][2].equals(cells[1][1]) && cells[1][1].equals(cells[2][0]))
            return new int[][]{{0,2},{1,1},{2,0}};

        return null;
    }
}
