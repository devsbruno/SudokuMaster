package com.sudokumaster;

import com.sudokumaster.model.SudokuBoard;
import com.sudokumaster.view.SudokuView;
import com.sudokumaster.controller.SudokuController;

/**
 * Main class to run the Sudoku application.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize model, view, and controller.
        SudokuBoard board = new SudokuBoard();
        SudokuView view = new SudokuView();
        SudokuController controller = new SudokuController(board, view);

        controller.showNewGameDialog();
    }
}
