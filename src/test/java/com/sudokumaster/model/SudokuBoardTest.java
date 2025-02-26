package com.sudokumaster.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SudokuBoard class.
 */
public class SudokuBoardTest {

    @Test
    public void testInitialBoardIsEmpty() {
        SudokuBoard board = new SudokuBoard();
        int[][] grid = board.getBoard();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                // Expecting all cells to be 0 (empty)
                assertEquals(0, grid[row][col], "Cell at (" + row + "," + col + ") should be empty.");
            }
        }
    }

    @Test
    public void testValidMove() {
        SudokuBoard board = new SudokuBoard();
        // Place a number in the cell and verify that it is a valid move initially
        assertTrue(board.placeNumber(0, 0, 5), "Placing number 5 at (0,0) should be valid.");
        // Trying to place the same number in the same row should now be invalid
        assertFalse(board.isValidMove(0, 1, 5), "Placing number 5 at (0,1) should be invalid (duplicate in row).");
    }
}
