package com.sudokumaster.model;

import java.util.Random;

/**
 * Sudoku puzzle generator that ensures every generated puzzle follows the rules.
 */
public class SudokuGenerator {

    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    private final Random random = new Random();

    public int[][] generatePuzzle(int difficulty) {
        int[][] board = new int[SIZE][SIZE];
        fillBoard(board);
        removeNumbers(board, difficulty);
        return board;
    }

    private void fillBoard(int[][] board) {
        // Ensures the board is completely filled with a valid Sudoku solution before numbers are removed.
        solve(board);
    }

    private void removeNumbers(int[][] board, int difficulty) {
        int cluesToRemove = switch (difficulty) {
            case 1 -> 40; // Easy (More clues)
            case 2 -> 50; // Medium
            case 3 -> 60; // Hard (Fewer clues)
            default -> 45;
        };

        for (int i = 0; i < cluesToRemove; i++) {
            int row, col;
            do {
                row = random.nextInt(SIZE);
                col = random.nextInt(SIZE);
            } while (board[row][col] == 0); // Ensure we're removing a non-empty cell

            board[row][col] = 0;
        }
    }

    private boolean solve(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValidPlacement(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board)) {
                                return true;
                            }
                            board[row][col] = 0; // Backtrack
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidPlacement(int[][] board, int row, int col, int num) {
        return isRowValid(board, row, num) && isColValid(board, col, num) && isBoxValid(board, row, col, num);
    }

    private boolean isRowValid(int[][] board, int row, int num) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean isColValid(int[][] board, int col, int num) {
        for (int row = 0; row < SIZE; row++) {
            if (board[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoxValid(int[][] board, int row, int col, int num) {
        int boxRowStart = (row / SUBGRID_SIZE) * SUBGRID_SIZE;
        int boxColStart = (col / SUBGRID_SIZE) * SUBGRID_SIZE;

        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (board[boxRowStart + i][boxColStart + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }
}
