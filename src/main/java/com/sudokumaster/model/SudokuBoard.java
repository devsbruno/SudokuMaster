package com.sudokumaster.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Sudoku game board with visual separation for 3x3 subgrids.
 */
public class SudokuBoard extends JPanel {

    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private final JTextField[][] cells = new JTextField[SIZE][SIZE];

    public SudokuBoard() {
        setLayout(new GridLayout(SIZE, SIZE));
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 20));
                cell.setBorder(createCellBorder(row, col));
                cells[row][col] = cell;
                add(cell);
            }
        }
    }

    private Border createCellBorder(int row, int col) {
        int top = (row % SUBGRID_SIZE == 0) ? 2 : 1;
        int left = (col % SUBGRID_SIZE == 0) ? 2 : 1;
        int bottom = (row == SIZE - 1) ? 2 : 1;
        int right = (col == SIZE - 1) ? 2 : 1;

        return BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK);
    }

    public void setCellValue(int row, int col, int value, boolean isFixed) {
        if (value != 0) {
            cells[row][col].setText(String.valueOf(value));
            cells[row][col].setEditable(!isFixed);
            cells[row][col].setForeground(isFixed ? Color.BLACK : Color.BLUE);
        } else {
            cells[row][col].setText("");
            cells[row][col].setEditable(true);
        }
    }

    public int[][] getUserInput() {
        int[][] board = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                try {
                    String text = cells[row][col].getText();
                    board[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    board[row][col] = 0;
                }
            }
        }
        return board;
    }
}
