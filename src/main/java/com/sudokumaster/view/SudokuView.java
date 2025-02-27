package com.sudokumaster.view;

import javax.swing.*;
import java.awt.*;

/**
 * Basic GUI for the Sudoku game.
 * This class sets up the main frame, board grid, numeric input buttons,
 * and an options menu with dark mode toggle, annotation mode, show guides, and new game options.
 */
public class SudokuView extends JFrame {

    private JButton[][] boardCells;
    private JPanel boardPanel;
    private JPanel numberPanel;
    private JMenuBar menuBar;
    private JCheckBoxMenuItem darkModeToggle;
    private JMenuItem newGameItem;
    private JCheckBoxMenuItem showGuidesToggle;
    // New: Annotation Mode toggle.
    private JCheckBoxMenuItem annotationModeToggle;

    public SudokuView() {
        super("Sudoku Master");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);

        initMenu();
        initBoard();
        initNumberPanel();

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(numberPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initMenu() {
        menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        darkModeToggle = new JCheckBoxMenuItem("Dark Mode");
        darkModeToggle.addActionListener(e -> toggleDarkMode(darkModeToggle.isSelected()));
        optionsMenu.add(darkModeToggle);

        // Add Annotation Mode toggle.
        annotationModeToggle = new JCheckBoxMenuItem("Annotation Mode");
        optionsMenu.add(annotationModeToggle);

        showGuidesToggle = new JCheckBoxMenuItem("Show Guides");
        optionsMenu.add(showGuidesToggle);

        newGameItem = new JMenuItem("New Game");
        optionsMenu.add(newGameItem);

        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }

    private void initBoard() {
        boardPanel = new JPanel(new GridLayout(9, 9));
        boardCells = new JButton[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JButton cell = new JButton("");
                cell.setFont(new Font("Arial", Font.BOLD, 20));
                boardCells[i][j] = cell;
                boardPanel.add(cell);
            }
        }
    }

    private void initNumberPanel() {
        numberPanel = new JPanel(new FlowLayout());
        for (int i = 1; i <= 9; i++) {
            JButton numberButton = new JButton(String.valueOf(i));
            numberButton.setFont(new Font("Arial", Font.BOLD, 18));
            numberPanel.add(numberButton);
        }
    }

    public void toggleDarkMode(boolean isDark) {
        Color backgroundColor = isDark ? Color.DARK_GRAY : Color.WHITE;
        Color foregroundColor = isDark ? Color.WHITE : Color.BLACK;
        boardPanel.setBackground(backgroundColor);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boardCells[i][j].setBackground(backgroundColor);
                boardCells[i][j].setForeground(foregroundColor);
            }
        }
        numberPanel.setBackground(backgroundColor);
        numberPanel.setForeground(foregroundColor);
    }

    /**
     * Overloaded updateBoard to display annotations for empty cells.
     * If a cell is empty and has annotations, displays them using HTML (small font).
     */
    public void updateBoard(int[][] board, java.util.Set<Integer>[][] annotations) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) {
                    boardCells[i][j].setText(String.valueOf(board[i][j]));
                    boardCells[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                } else if (!annotations[i][j].isEmpty()) {
                    StringBuilder sb = new StringBuilder("<html><small>");
                    java.util.List<Integer> sortedNotes = new java.util.ArrayList<>(annotations[i][j]);
                    java.util.Collections.sort(sortedNotes);
                    for (Integer note : sortedNotes) {
                        sb.append(note).append(" ");
                    }
                    sb.append("</small></html>");
                    boardCells[i][j].setText(sb.toString());
                    boardCells[i][j].setFont(new Font("Arial", Font.PLAIN, 12));
                } else {
                    boardCells[i][j].setText("");
                }
                boardCells[i][j].setBorder(UIManager.getBorder("Button.border"));
            }
        }
    }

    /**
     * Existing updateBoard method (without annotations).
     */
    public void updateBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardCells[i][j].setText(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                boardCells[i][j].setBorder(UIManager.getBorder("Button.border"));
            }
        }
    }

    public JButton[][] getBoardCells() {
        return boardCells;
    }

    public JPanel getNumberPanel() {
        return numberPanel;
    }

    public JCheckBoxMenuItem getDarkModeToggle() {
        return darkModeToggle;
    }

    public JMenuItem getNewGameItem() {
        return newGameItem;
    }

    public JCheckBoxMenuItem getShowGuidesToggle() {
        return showGuidesToggle;
    }

    public JCheckBoxMenuItem getAnnotationModeToggle() {
        return annotationModeToggle;
    }
}
