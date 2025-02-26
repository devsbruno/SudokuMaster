package com.sudokumaster.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utility class for generating Sudoku puzzles with a unique solution.
 *
 * Note: This implementation uses a backtracking algorithm to generate a complete board
 * and then removes a number of cells based on the selected difficulty.
 * Further validation for ensuring a unique solution should be implemented in a production system.
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

        // Determine number of cells to remove based on difficulty.
        int removals = switch (difficulty) {
            case EASY -> 30;
            case MEDIUM -> 40;
            case HARD -> 50;
        };

        // Remove cells randomly.
        removeNumbers(board.getBoard(), removals);

        return board;
    }

    /**
     * Uses backtracking to fill the board with a complete valid solution.
     *
     * @param board 2D int array representing the Sudoku board.
     * @return true if the board is successfully filled.
     **/
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
                    return false; // Backtrack if no valid number found.
                }
            }
        }
        return true; // Board filled.
    }

    /**
     * Checks if placing a number at the given row and column is valid.
     *
     * @param board  2D array representing the board.
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
        // Check 3x3 subgrid.
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
}
