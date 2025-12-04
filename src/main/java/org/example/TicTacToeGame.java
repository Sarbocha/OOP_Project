package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * GUI Tic-Tac-Toe game window.
 * Responsibilities:
 *  - display board UI and score labels
 *  - handle game lifecycle (moves, win detection, resets)
 *  - interact with Scoreboard (record results, show scoreboard)
 * Principles: single-responsibility methods, small helpers, clear separation of UI and model.
 */
public class TicTacToeGame extends JFrame {

    // UI / theme constants
    private static final Font CELL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 72);
    private static final Font SCORE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private static final Color CELL_BG = Color.WHITE;
    private static final Color CELL_FG = new Color(10, 95, 160);
    private static final Color WIN_BG = new Color(144, 238, 144); // light green
    private static final Color PANEL_BG = new Color(230, 230, 230);

    private final Board board;               // game model
    private final JButton[][] buttons;       // UI cells
    private boolean xTurn = true;            // turn tracker: true => X, false => O
    private String player1;
    private String player2;
    private final Scoreboard scoreboard;     // persistent scoreboard

    private final JLabel player1ScoreLabel = new JLabel();
    private final JLabel player2ScoreLabel = new JLabel();

    /**
     * Primary constructor
     */
    public TicTacToeGame(String player1, String player2) {
        this(player1, player2, new Scoreboard());
    }

    /**
     * Preferred constructor: pass an existing Scoreboard to avoid reloading multiple times.
     */
    public TicTacToeGame(String player1, String player2, Scoreboard scoreboard) {
        super("Tic-Tac-Toe");
        this.player1 = sanitizeName(player1, "Player1");
        this.player2 = sanitizeName(player2, "Player2");
        this.board = new Board();
        this.scoreboard = scoreboard;
        this.buttons = new JButton[3][3];

        initUi();
        updateScoreboardLabels();
        pack();
        setLocationRelativeTo(null);
    }

    /* ---------------------
       UI initialization
       --------------------- */

    private void initUi() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(PANEL_BG);

        setJMenuBar(buildMenuBar());
        add(buildBoardPanel(), BorderLayout.CENTER);
        add(buildScorePanel(), BorderLayout.SOUTH);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        JMenuItem newMatch = new JMenuItem("New Match");
        newMatch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newMatch.addActionListener(e -> startNewMatchFlow());

        JMenuItem viewBoard = new JMenuItem("View Scoreboard");
        viewBoard.addActionListener(e -> showScoreboardDialog());

        JMenuItem quit = new JMenuItem("Quit");
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        quit.addActionListener(e -> dispose());

        menu.add(newMatch);
        menu.add(viewBoard);
        menu.addSeparator();
        menu.add(quit);
        bar.add(menu);
        return bar;
    }

    private JPanel buildBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.setBackground(PANEL_BG);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton btn = createCellButton();
                final int r = i, c = j;
                btn.addActionListener(e -> onCellClicked(r, c, btn));
                buttons[i][j] = btn;
                panel.add(btn);
            }
        }
        return panel;
    }

    private JPanel buildScorePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));
        panel.setBackground(PANEL_BG);

        player1ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player1ScoreLabel.setFont(SCORE_FONT);

        player2ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player2ScoreLabel.setFont(SCORE_FONT);

        panel.add(player1ScoreLabel);
        panel.add(player2ScoreLabel);
        return panel;
    }

    private JButton createCellButton() {
        JButton b = new JButton("");
        b.setFont(CELL_FONT);
        b.setFocusPainted(false);
        b.setBackground(CELL_BG);
        b.setForeground(CELL_FG);
        return b;
    }

    /* ---------------------
       Game actions
       --------------------- */

    private void onCellClicked(int row, int col, JButton clicked) {
        if (!clicked.isEnabled() || !clicked.getText().isEmpty()) return;

        String mark = xTurn ? "X" : "O";
        clicked.setText(mark);

        if (!boardPositionSetSafe(mark, row, col)) {
            clicked.setText("");
            return;
        }

        int[][] winLine = board.checkWinnerLine();
        if (winLine != null) {
            disableAllButtons();
            highlightWinningLine(winLine);

            String winner = xTurn ? player1 : player2;
            String loser = xTurn ? player2 : player1;

            scoreboard.addWin(winner);
            scoreboard.addLoss(loser);
            scoreboard.save();
            updateScoreboardLabels();

            JOptionPane.showMessageDialog(this, winner + " wins!");
            postRoundMenu();
            return;
        }

        if (board.isFull()) {
            disableAllButtons();
            JOptionPane.showMessageDialog(this, "It's a draw!");
            postRoundMenu();
            return;
        }

        xTurn = !xTurn;
    }

    private boolean boardPositionSetSafe(String mark, int r, int c) {
        if (r < 0 || r >= 3 || c < 0 || c >= 3) return false;
        board.setCell(mark, new int[]{r, c});
        return true;
    }

    private void highlightWinningLine(int[][] line) {
        if (line == null) return;
        for (int[] pos : line) {
            int r = pos[0], c = pos[1];
            if (r >= 0 && r < 3 && c >= 0 && c < 3) {
                buttons[r][c].setBackground(WIN_BG);
            }
        }
    }

    private void disableAllButtons() {
        for (JButton[] row : buttons)
            for (JButton b : row)
                b.setEnabled(false);
    }

    private void enableAllButtons() {
        for (JButton[] row : buttons)
            for (JButton b : row) {
                b.setEnabled(true);
                b.setBackground(CELL_BG);
            }
    }

    private void resetBoard() {
        board.reset();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton b = buttons[i][j];
                b.setText("");
                b.setBackground(CELL_BG);
                b.setEnabled(true);
            }
        }
        xTurn = true;
    }

    /* ---------------------
       Scoreboard & players
       --------------------- */

    private void updateScoreboardLabels() {
        player1ScoreLabel.setText(String.format("%s  — Wins: %d  Losses: %d",
                player1, scoreboard.getWins(player1), scoreboard.getLosses(player1)));
        player2ScoreLabel.setText(String.format("%s  — Wins: %d  Losses: %d",
                player2, scoreboard.getWins(player2), scoreboard.getLosses(player2)));
    }

    private void startNewMatchFlow() {
        String p1 = (String) JOptionPane.showInputDialog(
                this, "Player 1 nickname:", "Rename Player",
                JOptionPane.QUESTION_MESSAGE, null, null, player1);

        String p2 = (String) JOptionPane.showInputDialog(
                this, "Player 2 nickname:", "Rename Player",
                JOptionPane.QUESTION_MESSAGE, null, null, player2);

        if (p1 != null && !p1.isBlank() && !p1.equals(player1)) {
            scoreboard.renamePlayer(player1, p1);
            player1 = sanitizeName(p1, player1);
        }
        if (p2 != null && !p2.isBlank() && !p2.equals(player2)) {
            scoreboard.renamePlayer(player2, p2);
            player2 = sanitizeName(p2, player2);
        }

        resetBoard();
        scoreboard.save();
        updateScoreboardLabels();
    }

    private String sanitizeName(String name, String fallback) {
        if (name == null) return fallback;
        String s = name.trim();
        return s.isEmpty() ? fallback : s;
    }

    /* ---------------------
       Post-Round Menu
       --------------------- */

    /**
     * After a round ends, ask user if they want to restart, new game, or exit.
     */
    private void postRoundMenu() {
        String[] options = {"Restart", "New Game", "Exit"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Round finished. What would you like to do?",
                "Next Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            resetBoard(); // Restart same players
        }
        else if (choice == 1) {
            startNewMatchFlow(); // New players
        }
        else {
            dispose(); // Exit
        }
    }

    private void showScoreboardDialog() {
        java.util.List<String> players = scoreboard.getTopPlayers();
        String[] cols = {"Player", "Wins", "Losses"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        for (String p : players) {
            model.addRow(new Object[]{p, scoreboard.getWins(p), scoreboard.getLosses(p)});
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(320, Math.min(400, 30 + players.size() * 24)));

        JOptionPane.showMessageDialog(this, scroll, "Scoreboard", JOptionPane.PLAIN_MESSAGE);
    }
}
