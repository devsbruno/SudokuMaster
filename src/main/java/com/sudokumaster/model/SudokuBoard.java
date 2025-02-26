package com.sudokumaster.model;

import java.util.Arrays;

/**
 * Represents a Sudoku board.
 * This class encapsulates the board state and provides methods to validate moves,
 * check if the puzzle is solved, and reset the board.
 */
public class SudokuBoard {
    private final int size = 9;
    // 2D array representing the current state of the board (0 indicates an empty cell)
    private final int[][] board;

    /**
     * Constructor: Initializes a 9x9 board with all cells set to 0.
     */
    public SudokuBoard() {
        board = new int[size][size];
        for (int[] row : board) {
            Arrays.fill(row, 0);
        }
    }

    /**
     * Gets the board size.
     *
     * @return the size of the board (9).
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the current board state.
     *
     * @return a 2D int array representing the board.
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Checks if placing a number at the specified position is valid according to Sudoku rules.
     *
     * @param row    Row index (0-8).
     * @param col    Column index (0-8).
     * @param number Number to place (1-9).
     * @return true if the move is valid, false otherwise.
     */
    public boolean isValidMove(int row, int col, int number) {
        // Check the row and column.
        for (int i = 0; i < size; i++) {
            if (board[row][i] == number || board[i][col] == number) {
                return false;
            }
        }
        // Check the 3x3 block.
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
     * @param row    Row index (0-8).
     * @param col    Column index (0-8).
     * @param number Number to place (1-9).
     * @return true if the number was successfully placed, false otherwise.
     */
    public boolean placeNumber(int row, int col, int number) {
        if (isValidMove(row, col, number)) {
            board[row][col] = number;
            return true;
        }
        return false;
    }

    /**
     * Checks if the puzzle is solved.
     * The board is considered solved if there are no empty cells and no rule violations.
     *
     * @return true if the board is completely and correctly filled, false otherwise.
     */
    public boolean isSolved() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int number = board[i][j];
                if (number == 0) {
                    return false;
                }
                // Check row and column (ignoring the current cell).
                for (int k = 0; k < size; k++) {
                    if ((k != j && board[i][k] == number) ||
                            (k != i && board[k][j] == number)) {
                        return false;
                    }
                }
                // Check 3x3 block.
                int blockRowStart = (i / 3) * 3;
                int blockColStart = (j / 3) * 3;
                for (int r = blockRowStart; r < blockRowStart + 3; r++) {
                    for (int c = blockColStart; c < blockColStart + 3; c++) {
                        if ((r != i || c != j) && board[r][c] == number) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Resets the board to the provided initial state.
     *
     * @param newState 2D int array representing the initial puzzle state.
     */
    public void resetBoard(int[][] newState) {
        for (int i = 0; i < size; i++) {
            System.arraycopy(newState[i], 0, board[i], 0, size);
        }
    }
}
