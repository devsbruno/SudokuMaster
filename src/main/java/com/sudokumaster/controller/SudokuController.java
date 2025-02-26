package com.sudokumaster.controller;

import com.sudokumaster.model.SudokuBoard;
import com.sudokumaster.view.SudokuView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class that coordinates interactions between the model and the view.
 * It attaches event listeners to the numeric buttons and will later handle cell selections.
 */
public class SudokuController {

    private final SudokuBoard board;
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
     * Attaches action listeners to the number buttons.
     */
    private void initController() {
        for (Component comp : view.getNumberPanel().getComponents()) {
            if (comp instanceof JButton) {
                JButton numberButton = (JButton) comp;
                numberButton.addActionListener(new NumberButtonListener());
            }
        }
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
}
