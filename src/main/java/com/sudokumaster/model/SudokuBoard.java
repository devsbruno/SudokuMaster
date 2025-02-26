package com.sudokumaster.model;

import java.util.Arrays;

/**
 * Represents a Sudoku board.
 * This class encapsulates the board state and provides methods to validate moves
 * and manipulate the board according to standard Sudoku rules.
 */
public class SudokuBoard {
    // The size of the board (9x9)
    private final int size = 9;
    // 2D array to represent the board cells (0 means empty)
    private final int[][] board;

    /**
     * Constructor: Initializes a 9x9 board with all cells set to 0.
     */
    public SudokuBoard() {
        board = new int[size][size];
        // Fill the board with zeros (empty cells)
        for (int[] row : board) {
            Arrays.fill(row, 0);
        }
    }

    /**
     * Gets the size of the board.
     *
     * @return the board size (9)
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the current state of the board.
     *
     * @return a 2D int array representing the board.
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Checks if placing a number at the given row and column is valid according to Sudoku rules.
     *
     * @param row    Row index (0-8)
     * @param col    Column index (0-8)
     * @param number Number to place (1-9)
     * @return true if the move is valid, false otherwise.
     */
    public boolean isValidMove(int row, int col, int number) {
        // Check the row for duplicate
        for (int i = 0; i < size; i++) {
            if (board[row][i] == number) {
                return false;
            }
        }

        // Check the column for duplicate
        for (int i = 0; i < size; i++) {
            if (board[i][col] == number) {
                return false;
            }
        }

        // Check the 3x3 block for duplicate
        int blockRowStart = (row / 3) * 3;
        int blockColStart = (col / 3) * 3;
        for (int i = blockRowStart; i < blockRowStart + 3; i++) {
            for (int j = blockColStart; j < blockColStart + 3; j++) {
                if (board[i][j] == number) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Attempts to place a number on the board if the move is valid.
     *
     * @param row    Row index (0-8)
     * @param col    Column index (0-8)
     * @param number Number to place (1-9)
     * @return true if the number was successfully placed, false otherwise.
     */
    public boolean placeNumber(int row, int col, int number) {
        if (isValidMove(row, col, number)) {
            board[row][col] = number;
            return true;
        }
        return false;
    }

    // Future methods for generating puzzles and further manipulation can be added here.
}
