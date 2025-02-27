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
 * It handles numeric input, board cell selection, game flow, and annotation mode.
 */
public class SudokuController {

    private SudokuBoard board;
    private final SudokuView view;
    private int selectedRow = -1;
    private int selectedCol = -1;
    // Stores the fixed numbers of the puzzle (the initial board state).
    private int[][] fixedBoard;

    public SudokuController(SudokuBoard board, SudokuView view) {
        this.board = board;
        this.view = view;
        initController();
    }

    private void initController() {
        // Attach listeners to number buttons.
        for (Component comp : view.getNumberPanel().getComponents()) {
            if (comp instanceof JButton) {
                JButton numberButton = (JButton) comp;
                numberButton.addActionListener(new NumberButtonListener());
            }
        }
        view.getNewGameItem().addActionListener(e -> showNewGameDialog());
        // Attach listeners to board cells.
        JButton[][] boardCells = view.getBoardCells();
        for (int row = 0; row < boardCells.length; row++) {
            for (int col = 0; col < boardCells[row].length; col++) {
                boardCells[row][col].addActionListener(new BoardCellListener(row, col));
            }
        }
    }

    private class BoardCellListener implements ActionListener {
        private final int row;
        private final int col;

        public BoardCellListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Remove highlight from previously selected cell.
            if (selectedRow != -1 && selectedCol != -1) {
                view.getBoardCells()[selectedRow][selectedCol].setBorder(UIManager.getBorder("Button.border"));
            }
            selectedRow = row;
            selectedCol = col;
            JButton cell = view.getBoardCells()[row][col];
            cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            updateGuides();
        }
    }

    private class NumberButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedRow == -1 || selectedCol == -1) {
                JOptionPane.showMessageDialog(view, "Please select a cell first.", "No Cell Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JButton source = (JButton) e.getSource();
            int number = Integer.parseInt(source.getText());
            // If Annotation Mode is active, toggle annotation.
            if (view.getAnnotationModeToggle().isSelected()) {
                board.toggleAnnotation(selectedRow, selectedCol, number);
            } else {
                view.getBoardCells()[selectedRow][selectedCol].setForeground(Color.BLACK);
                if (board.isValidMove(selectedRow, selectedCol, number)) {
                    board.placeNumber(selectedRow, selectedCol, number);
                } else {
                    // Show error: display number in red.
                    JButton selectedCell = view.getBoardCells()[selectedRow][selectedCol];
                    selectedCell.setText(String.valueOf(number));
                    selectedCell.setForeground(Color.RED);
                }
            }
            // Update the board display using the fixed board for color differentiation.
            view.updateBoard(board.getBoard(), fixedBoard, board.getAnnotations());
            updateGuides();
            // Check for puzzle completion if not in annotation mode.
            if (!view.getAnnotationModeToggle().isSelected() && board.isSolved()) {
                showGameCompletedDialog();
            }
        }
    }

    private void updateGuides() {
        if (selectedRow == -1 || selectedCol == -1) {
            resetNumberButtonHighlights();
            return;
        }
        if (view.getShowGuidesToggle().isSelected()) {
            for (Component comp : view.getNumberPanel().getComponents()) {
                if (comp instanceof JButton) {
                    JButton numberButton = (JButton) comp;
                    int number = Integer.parseInt(numberButton.getText());
                    if (board.isValidMove(selectedRow, selectedCol, number)) {
                        numberButton.setBackground(new Color(144, 238, 144));
                    } else {
                        numberButton.setBackground(UIManager.getColor("Button.background"));
                    }
                }
            }
        } else {
            resetNumberButtonHighlights();
        }
    }

    private void resetNumberButtonHighlights() {
        for (Component comp : view.getNumberPanel().getComponents()) {
            if (comp instanceof JButton) {
                ((JButton) comp).setBackground(UIManager.getColor("Button.background"));
            }
        }
    }

    public void showNewGameDialog() {
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
            // Store a deep copy of the generated puzzle as fixed numbers.
            fixedBoard = deepCopy(board.getBoard());
            view.updateBoard(board.getBoard(), fixedBoard, board.getAnnotations());
            selectedRow = -1;
            selectedCol = -1;
            resetNumberButtonHighlights();
        }
    }

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
            board.resetBoard(deepCopy(fixedBoard));
            view.updateBoard(board.getBoard(), fixedBoard, board.getAnnotations());
            resetNumberButtonHighlights();
        } else if (choice == JOptionPane.NO_OPTION) {
            showNewGameDialog();
        }
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[0].length);
        }
        return copy;
    }
}
