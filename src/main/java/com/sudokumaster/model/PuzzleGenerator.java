package com.sudokumaster.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utility class for generating Sudoku puzzles with a unique solution.
 * This implementation uses backtracking to generate a complete board,
 * then removes cells based on the selected difficulty.
 */
public class PuzzleGenerator {

    private static final Random random = new Random();

    /**
     * Generates a new Sudoku puzzle based on the provided difficulty.
     *
     * @param difficulty the selected difficulty level.
     * @return a SudokuBoard representing the puzzle with some cells removed.
     */
    public static SudokuBoard generatePuzzle(Difficulty difficulty) {
        SudokuBoard board = new SudokuBoard();
        // Fill board completely with a valid solution using backtracking.
        fillBoard(board.getBoard());

        // Determine the number of cells to remove based on difficulty.
        int removals = 0;
        switch (difficulty) {
            case EASY:
                removals = 30;
                break;
            case MEDIUM:
                removals = 40;
                break;
            case HARD:
                removals = 50;
                break;
        }

        // Remove cells randomly.
        removeNumbers(board.getBoard(), removals);

        // Validate the board to ensure it adheres to Sudoku rules.
        // If validation fails, regenerate the puzzle.
        if (!validateBoard(board.getBoard())) {
            return generatePuzzle(difficulty);
        }

        return board;
    }

    /**
     * Uses backtracking to fill the board with a complete valid solution.
     *
     * @param board 2D int array representing the Sudoku board.
     * @return true if the board is successfully filled.
     */
    private static boolean fillBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    List<Integer> numbers = getShuffledNumbers();
                    for (int number : numbers) {
                        if (isValidMove(board, row, col, number)) {
                            board[row][col] = number;
                            if (fillBoard(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false; // Trigger backtracking.
                }
            }
        }
        return true; // All cells filled.
    }

    /**
     * Checks if placing a number at the given row and column is valid.
     *
     * @param board  2D int array representing the board.
     * @param row    Row index.
     * @param col    Column index.
     * @param number Number to place.
     * @return true if valid, false otherwise.
     */
    private static boolean isValidMove(int[][] board, int row, int col, int number) {
        // Check row and column.
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == number || board[i][col] == number) {
                return false;
            }
        }
        // Check the 3x3 subgrid.
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
     * Returns a list of numbers 1 through 9 in random order.
     *
     * @return List of integers 1-9 shuffled.
     */
    private static List<Integer> getShuffledNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers, random);
        return numbers;
    }

    /**
     * Removes a specified number of cells randomly from the board.
     *
     * @param board    2D int array representing the board.
     * @param removals Number of cells to remove.
     */
    private static void removeNumbers(int[][] board, int removals) {
        int count = 0;
        while (count < removals) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                count++;
            }
        }
    }

    /**
     * Validates the board to ensure that each row, column, and 3x3 block contains unique numbers.
     * This method checks only non-zero entries.
     *
     * @param board the board to validate.
     * @return true if the board is valid, false otherwise.
     */
    public static boolean validateBoard(int[][] board) {
        // Validate rows and columns.
        for (int i = 0; i < 9; i++) {
            boolean[] rowCheck = new boolean[10]; // indices 1-9.
            boolean[] colCheck = new boolean[10];
            for (int j = 0; j < 9; j++) {
                int rowVal = board[i][j];
                int colVal = board[j][i];
                if (rowVal != 0) {
                    if (rowCheck[rowVal]) {
                        return false;
                    }
                    rowCheck[rowVal] = true;
                }
                if (colVal != 0) {
                    if (colCheck[colVal]) {
                        return false;
                    }
                    colCheck[colVal] = true;
                }
            }
        }
        // Validate 3x3 blocks.
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                boolean[] blockCheck = new boolean[10];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int val = board[blockRow * 3 + i][blockCol * 3 + j];
                        if (val != 0) {
                            if (blockCheck[val]) {
                                return false;
                            }
                            blockCheck[val] = true;
                        }
                    }
                }
            }
        }
        return true;
    }
}
