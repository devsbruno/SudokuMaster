package com.sudokumaster.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Sudoku board.
 * This class encapsulates the board state and provides methods to validate moves,
 * manage annotations, check if the puzzle is solved, and reset the board.
 */
public class SudokuBoard {
    private final int size = 9;
    private final int[][] board;
    // 2D array for cell annotations (manual/automatic)
    private final Set<Integer>[][] annotations;

    @SuppressWarnings("unchecked")
    public SudokuBoard() {
        board = new int[size][size];
        annotations = new HashSet[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(board[i], 0);
            for (int j = 0; j < size; j++) {
                annotations[i][j] = new HashSet<>();
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int[][] getBoard() {
        return board;
    }

    public Set<Integer>[][] getAnnotations() {
        return annotations;
    }

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
     * Places a number if the move is valid.
     * Clears any annotations for that cell and updates related cells.
     */
    public boolean placeNumber(int row, int col, int number) {
        if (isValidMove(row, col, number)) {
            board[row][col] = number;
            clearAnnotations(row, col);
            updateAnnotationsAfterMove(row, col, number);
            return true;
        }
        return false;
    }

    /**
     * Adds an annotation to the specified cell.
     */
    public void addAnnotation(int row, int col, int number) {
        if (board[row][col] == 0 && number >= 1 && number <= 9) {
            annotations[row][col].add(number);
        }
    }

    /**
     * Removes an annotation from the specified cell.
     */
    public void removeAnnotation(int row, int col, int number) {
        if (board[row][col] == 0) {
            annotations[row][col].remove(number);
        }
    }

    /**
     * Toggles an annotation in the specified cell.
     */
    public void toggleAnnotation(int row, int col, int number) {
        if (board[row][col] == 0) {
            if (annotations[row][col].contains(number)) {
                annotations[row][col].remove(number);
            } else {
                annotations[row][col].add(number);
            }
        }
    }

    /**
     * Clears all annotations from the specified cell.
     */
    public void clearAnnotations(int row, int col) {
        annotations[row][col].clear();
    }

    /**
     * After placing a number, remove that number from annotations in the same row, column, and block.
     */
    public void updateAnnotationsAfterMove(int row, int col, int number) {
        // Row and column.
        for (int i = 0; i < size; i++) {
            if (board[row][i] == 0) {
                annotations[row][i].remove(number);
            }
            if (board[i][col] == 0) {
                annotations[i][col].remove(number);
            }
        }
        // 3x3 block.
        int blockRowStart = (row / 3) * 3;
        int blockColStart = (col / 3) * 3;
        for (int i = blockRowStart; i < blockRowStart + 3; i++) {
            for (int j = blockColStart; j < blockColStart + 3; j++) {
                if (board[i][j] == 0) {
                    annotations[i][j].remove(number);
                }
            }
        }
    }

    public boolean isSolved() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int number = board[i][j];
                if (number == 0) {
                    return false;
                }
                // Check row and column uniqueness.
                for (int k = 0; k < size; k++) {
                    if ((k != j && board[i][k] == number) ||
                            (k != i && board[k][j] == number)) {
                        return false;
                    }
                }
                // Check 3x3 block uniqueness.
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

    public void resetBoard(int[][] newState) {
        for (int i = 0; i < size; i++) {
            System.arraycopy(newState[i], 0, board[i], 0, size);
            for (int j = 0; j < size; j++) {
                annotations[i][j].clear();
            }
        }
    }
}
