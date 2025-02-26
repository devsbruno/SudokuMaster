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
 * It attaches event listeners to numeric buttons, board cells, new game option,
 * and handles guide highlighting for valid moves.
 */
public class SudokuController {

    private SudokuBoard board;
    private final SudokuView view;
    private int selectedRow = -1;
    private int selectedCol = -1;
    // Stores the initial state of the current puzzle for restarting.
    private int[][] initialBoardState;

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
     * Attaches action listeners to numeric buttons, board cells, and the new game menu item.
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
     * Listener for board cell clicks, handling cell selection and highlighting.
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
            // Remove border from previously selected cell.
            if (selectedRow != -1 && selectedCol != -1) {
                view.getBoardCells()[selectedRow][selectedCol].setBorder(UIManager.getBorder("Button.border"));
            }
            selectedRow = row;
            selectedCol = col;
            // Highlight the selected cell with a blue border.
            JButton cell = view.getBoardCells()[row][col];
            cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            // Update guides if the option is enabled.
            updateGuides();
        }
    }

    /**
     * Listener for number buttons.
     * Inserts the number into the selected cell if valid; otherwise, shows it in red.
     * After a valid move, checks if the puzzle is solved.
     */
    private class NumberButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedRow == -1 || selectedCol == -1) {
                JOptionPane.showMessageDialog(view, "Please select a cell first.", "No Cell Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JButton source = (JButton) e.getSource();
            int number = Integer.parseInt(source.getText());
            // Reset cell's text color to black.
            view.getBoardCells()[selectedRow][selectedCol].setForeground(Color.BLACK);

            if (board.isValidMove(selectedRow, selectedCol, number)) {
                board.placeNumber(selectedRow, selectedCol, number);
                view.updateBoard(board.getBoard());
                // Check if the puzzle is completed.
                if (board.isSolved()) {
                    showGameCompletedDialog();
                }
            } else {
                // Invalid move: display the number in red.
                JButton selectedCell = view.getBoardCells()[selectedRow][selectedCol];
                selectedCell.setText(String.valueOf(number));
                selectedCell.setForeground(Color.RED);
            }
            // After insertion, update the guides.
            updateGuides();
        }
    }

    /**
     * Updates the guide highlighting for numeric buttons based on the selected cell.
     * If "Show Guides" is enabled, highlights buttons corresponding to valid moves.
     */
    private void updateGuides() {
        // If no cell is selected, reset all number buttons.
        if (selectedRow == -1 || selectedCol == -1) {
            resetNumberButtonHighlights();
            return;
        }
        // Check if "Show Guides" is enabled.
        if (view.getShowGuidesToggle().isSelected()) {
            Component[] components = view.getNumberPanel().getComponents();
            for (Component comp : components) {
                if (comp instanceof JButton) {
                    JButton numberButton = (JButton) comp;
                    int number = Integer.parseInt(numberButton.getText());
                    if (board.isValidMove(selectedRow, selectedCol, number)) {
                        // Highlight valid moves with a light green background.
                        numberButton.setBackground(new Color(144, 238, 144));
                    } else {
                        // Reset to default background.
                        numberButton.setBackground(UIManager.getColor("Button.background"));
                    }
                }
            }
        } else {
            // If guide option is not enabled, ensure all buttons have default background.
            resetNumberButtonHighlights();
        }
    }

    /**
     * Resets the background of all number buttons to the default color.
     */
    private void resetNumberButtonHighlights() {
        Component[] components = view.getNumberPanel().getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                ((JButton) comp).setBackground(UIManager.getColor("Button.background"));
            }
        }
    }

    /**
     * Opens a dialog to select difficulty and generate a new puzzle.
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
            board = PuzzleGenerator.generatePuzzle(selectedDifficulty);
            view.updateBoard(board.getBoard());
            // Reset cell selection.
            selectedRow = -1;
            selectedCol = -1;
            resetNumberButtonHighlights();
            // Store a deep copy of the initial puzzle state for restarting.
            initialBoardState = deepCopy(board.getBoard());
        }
    }

    /**
     * Displays a dialog upon puzzle completion, offering to restart the current puzzle or start a new game.
     */
    private void showGameCompletedDialog() {
        Object[] options = {"Restart", "New Game"};
        int choice = JOptionPane.showOptionDialog(
                view,
                "Congratulations! You completed the puzzle!",
                "Game Completed",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (choice == JOptionPane.YES_OPTION) {
            // Restart: reset the board to the initial puzzle state.
            board.resetBoard(deepCopy(initialBoardState));
            view.updateBoard(board.getBoard());
            resetNumberButtonHighlights();
        } else if (choice == JOptionPane.NO_OPTION) {
            // Start a new game.
            showNewGameDialog();
        }
    }

    /**
     * Utility method to create a deep copy of a 2D array.
     *
     * @param original the original 2D array.
     * @return a deep copy of the array.
     */
    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[0].length);
        }
        return copy;
    }
}
