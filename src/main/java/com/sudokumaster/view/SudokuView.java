package com.sudokumaster.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

        // Annotation Mode toggle (for annotation support)
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
                // Define borders: thicker borders at the beginning of each 3x3 block.
                int top = (i % 3 == 0) ? 3 : 1;
                int left = (j % 3 == 0) ? 3 : 1;
                int bottom = (i == 8) ? 3 : 1;
                int right = (j == 8) ? 3 : 1;
                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
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
     * Simple updateBoard method using the current board state.
     */
    public void updateBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardCells[i][j].setText(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
            }
        }
    }

    /**
     * Overloaded updateBoard method.
     * Differentiates fixed numbers (displayed in BLACK) from user inputs (displayed in DARK BLUE).
     * Displays annotations (if any) in GRAY.
     *
     * @param board       The current board state.
     * @param fixedBoard  The initial puzzle state (fixed numbers).
     * @param annotations The annotations for each cell.
     */
    public void updateBoard(int[][] board, int[][] fixedBoard, Set<Integer>[][] annotations) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) {
                    boardCells[i][j].setText(String.valueOf(board[i][j]));
                    if (fixedBoard != null && board[i][j] == fixedBoard[i][j]) {
                        boardCells[i][j].setForeground(Color.BLACK);
                    } else {
                        boardCells[i][j].setForeground(new Color(0, 0, 250)); // User input in dark blue.
                    }
                    boardCells[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                } else if (annotations != null && !annotations[i][j].isEmpty()) {
                    StringBuilder sb = new StringBuilder("<html><small>");
                    List<Integer> sortedNotes = new ArrayList<>(annotations[i][j]);
                    Collections.sort(sortedNotes);
                    for (Integer note : sortedNotes) {
                        sb.append(note).append(" ");
                    }
                    sb.append("</small></html>");
                    boardCells[i][j].setText(sb.toString());
                    boardCells[i][j].setFont(new Font("Arial", Font.PLAIN, 12));
                    boardCells[i][j].setForeground(Color.GRAY);
                } else {
                    boardCells[i][j].setText("");
                }
                // Reapply the cell borders.
                int top = (i % 3 == 0) ? 3 : 1;
                int left = (j % 3 == 0) ? 3 : 1;
                int bottom = (i == 8) ? 3 : 1;
                int right = (j == 8) ? 3 : 1;
                boardCells[i][j].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
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
