package com.sudokumaster.view;

import javax.swing.*;
import java.awt.*;

/**
 * Basic GUI for the Sudoku game.
 * This class sets up the main frame, board grid, numeric input buttons,
 * and an options menu with a dark mode toggle, new game option, and guide toggle.
 */
public class SudokuView extends JFrame {

    private JButton[][] boardCells;
    private JPanel boardPanel;
    private JPanel numberPanel;
    private JMenuBar menuBar;
    private JCheckBoxMenuItem darkModeToggle;
    private JMenuItem newGameItem;
    // New: Toggle for showing guides (highlighting valid moves)
    private JCheckBoxMenuItem showGuidesToggle;

    /**
     * Constructor: Initializes the Sudoku game window.
     */
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

    /**
     * Initializes the menu bar and adds a dark mode toggle, show guides option, and new game option.
     */
    private void initMenu() {
        menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        darkModeToggle = new JCheckBoxMenuItem("Dark Mode");
        darkModeToggle.addActionListener(e -> toggleDarkMode(darkModeToggle.isSelected()));
        optionsMenu.add(darkModeToggle);

        // New: Add the Show Guides toggle
        showGuidesToggle = new JCheckBoxMenuItem("Show Guides");
        optionsMenu.add(showGuidesToggle);

        // Add the New Game menu item
        newGameItem = new JMenuItem("New Game");
        optionsMenu.add(newGameItem);

        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Initializes the board panel with a 9x9 grid of buttons.
     */
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

    /**
     * Initializes the panel containing numeric input buttons (1-9).
     */
    private void initNumberPanel() {
        numberPanel = new JPanel(new FlowLayout());
        for (int i = 1; i <= 9; i++) {
            JButton numberButton = new JButton(String.valueOf(i));
            numberButton.setFont(new Font("Arial", Font.BOLD, 18));
            numberPanel.add(numberButton);
        }
    }

    /**
     * Toggles between light and dark mode.
     *
     * @param isDark true to enable dark mode, false for light mode.
     */
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
     * Updates the board display based on the provided board state.
     *
     * @param board 2D int array representing the current state of the Sudoku board.
     */
    public void updateBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardCells[i][j].setText(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                // Reset the cell's border to default after board update.
                boardCells[i][j].setBorder(UIManager.getBorder("Button.border"));
            }
        }
    }

    // Getters for UI components to be used by the controller
    public JButton[][] getBoardCells() {
        return boardCells;
    }

    public JPanel getNumberPanel() {
        return numberPanel;
    }

    public JCheckBoxMenuItem getDarkModeToggle() {
        return darkModeToggle;
    }

    /**
     * Getter for the New Game menu item.
     *
     * @return JMenuItem for starting a new game.
     */
    public JMenuItem getNewGameItem() {
        return newGameItem;
    }

    /**
     * Getter for the Show Guides toggle.
     *
     * @return JCheckBoxMenuItem for enabling/disabling guide highlighting.
     */
    public JCheckBoxMenuItem getShowGuidesToggle() {
        return showGuidesToggle;
    }
}
