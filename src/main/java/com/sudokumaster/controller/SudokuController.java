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
 * It attaches event listeners to the numeric buttons, board cells, and the new game option.
 */
public class SudokuController {

    private SudokuBoard board;
    private final SudokuView view;
    private int selectedRow = -1;
    private int selectedCol = -1;

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
     * Attaches action listeners to the number buttons, board cells, and the new game menu item.
     */
    private void initController() {
        // Attach listeners to numeric buttons.
        for (Component comp : view.getNumberPanel().getComponents()) {
            if (comp instanceof JButton) {
                JButton numberButton = (JButton) comp;
                numberButton.addActionListener(new NumberButtonListener());
            }
        }
        // Attach listener to the New Game menu item.
        view.getNewGameItem().addActionListener(e -> showNewGameDialog());

        // Attach listeners to board cells for selection.
        JButton[][] boardCells = view.getBoardCells();
        for (int row = 0; row < boardCells.length; row++) {
            for (int col = 0; col < boardCells[row].length; col++) {
                boardCells[row][col].addActionListener(new BoardCellListener(row, col));
            }
        }
    }

    /**
     * Listener for board cell clicks. Handles cell selection.
     */
    private class BoardCellListener implements ActionListener {
        private final int row;
        private final int col;

        public BoardCellListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Reset previous selected cell border, if any.
            if (selectedRow != -1 && selectedCol != -1) {
                view.getBoardCells()[selectedRow][selectedCol].setBorder(UIManager.getBorder("Button.border"));
            }
            selectedRow = row;
            selectedCol = col;
            // Highlight the newly selected cell.
            JButton cell = view.getBoardCells()[row][col];
            cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        }
    }

    /**
     * Listener for number buttons.
     * If a cell is selected, attempts to insert the number.
     * If the move is invalid, displays the number in red.
     */
    private class NumberButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedRow == -1 || selectedCol == -1) {
                // No cell selected; do nothing or alert the user.
                JOptionPane.showMessageDialog(view, "Please select a cell first.", "No Cell Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JButton source = (JButton) e.getSource();
            int number = Integer.parseInt(source.getText());

            // Reset the cell's foreground color to default (black).
            view.getBoardCells()[selectedRow][selectedCol].setForeground(Color.BLACK);

            if (board.isValidMove(selectedRow, selectedCol, number)) {
                // Valid move: update the model.
                board.placeNumber(selectedRow, selectedCol, number);
                view.updateBoard(board.getBoard());
            } else {
                // Invalid move: show the number in red on the selected cell.
                JButton selectedCell = view.getBoardCells()[selectedRow][selectedCol];
                selectedCell.setText(String.valueOf(number));
                selectedCell.setForeground(Color.RED);
            }
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
            com.sudokumaster.model.Difficulty selectedDifficulty;
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
            // Generate new puzzle using the selected difficulty.
            board = PuzzleGenerator.generatePuzzle(selectedDifficulty);
            view.updateBoard(board.getBoard());
            // Reset any cell selection.
            selectedRow = -1;
            selectedCol = -1;
        }
    }
}
