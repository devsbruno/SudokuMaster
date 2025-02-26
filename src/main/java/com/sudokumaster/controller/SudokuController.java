package com.sudokumaster.controller;

import com.sudokumaster.model.Difficulty;
import com.sudokumaster.model.PuzzleGenerator;
import com.sudokumaster.model.SudokuBoard;
import com.sudokumaster.view.SudokuView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class that coordinates interactions between the model and the view.
 * It attaches event listeners to the numeric buttons and the new game option.
 */
public class SudokuController {

    private SudokuBoard board;
    private final SudokuView view;

    /**
     * Constructor: Initializes the controller with the given model and view.
     *
     * @param board the Sudoku board model.
     * @param view  the GUI view.
     */
    public SudokuController(SudokuBoard board, SudokuView view) {
        this.board = board;
        this.view = view;
        initController();
    }

    /**
     * Attaches action listeners to the number buttons and the new game menu item.
     */
    private void initController() {
        // Attach listeners to numeric buttons
        for (Component comp : view.getNumberPanel().getComponents()) {
            if (comp instanceof JButton) {
                JButton numberButton = (JButton) comp;
                numberButton.addActionListener(new NumberButtonListener());
            }
        }
        // Attach listener to the New Game menu item
        view.getNewGameItem().addActionListener(e -> showNewGameDialog());
    }

    /**
     * Listener for number buttons.
     * Currently, it prints the selected number to the console.
     * Future implementation: update the selected cell in the board using the model.
     */
    private class NumberButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String numberStr = source.getText();
            System.out.println("Number selected: " + numberStr);
            // Future implementation: integrate with cell selection and update board accordingly.
        }
    }

    /**
     * Opens a dialog for the user to select the difficulty and generates a new puzzle.
     */
    private void showNewGameDialog() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(
                view,
                "Select difficulty:",
                "New Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice >= 0) {
            Difficulty selectedDifficulty;
            switch (choice) {
                case 0:
                    selectedDifficulty = Difficulty.EASY;
                    break;
                case 1:
                    selectedDifficulty = Difficulty.MEDIUM;
                    break;
                case 2:
                    selectedDifficulty = Difficulty.HARD;
                    break;
                default:
                    selectedDifficulty = Difficulty.EASY;
                    break;
            }
            // Generate new puzzle using the selected difficulty
            board = PuzzleGenerator.generatePuzzle(selectedDifficulty);
            // Update the view with the new board state
            view.updateBoard(board.getBoard());
        }
    }
}
