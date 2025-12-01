package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tic-Tac-Toe game with GUI, scoreboard, and menu for new match / quit.
 */
public class TicTacToeGame extends JFrame implements ActionListener {

    private final Board board;
    private final JButton[][] buttons;
    private boolean xTurn = true;
    private String player1;
    private String player2;
    private final Scoreboard scoreboard;

    private final JLabel player1Score = new JLabel();
    private final JLabel player2Score = new JLabel();

    public TicTacToeGame(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Board();
        this.scoreboard = new Scoreboard();
        this.buttons = new JButton[3][3];

        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());
        add(createBoardPanel(), BorderLayout.CENTER);
        add(createScorePanel(), BorderLayout.SOUTH);

        updateScoreboardLabels();
    }

    /** Menu bar with New Match and Quit options */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        JMenuItem newMatch = new JMenuItem("New Match");
        newMatch.addActionListener(e -> startNewMatch());

        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(e -> System.exit(0));

        menu.add(newMatch);
        menu.add(quit);
        menuBar.add(menu);

        return menuBar;
    }

    private void startNewMatch() {
        String p1 = JOptionPane.showInputDialog(this, "Enter Player 1 nickname:", player1);
        if (p1 != null && !p1.isBlank()) player1 = p1;

        String p2 = JOptionPane.showInputDialog(this, "Enter Player 2 nickname:", player2);
        if (p2 != null && !p2.isBlank()) player2 = p2;

        resetBoard();
        updateScoreboardLabels();
    }

    private JPanel createBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.BLACK);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton b = new JButton();
                b.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
                b.setFocusPainted(false);
                b.setBackground(Color.WHITE);
                b.setForeground(Color.BLUE);
                b.addActionListener(this);
                buttons[i][j] = b;
                panel.add(b);
            }
        }
        return panel;
    }

    private JPanel createScorePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        player1Score.setHorizontalAlignment(SwingConstants.CENTER);
        player1Score.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        player2Score.setHorizontalAlignment(SwingConstants.CENTER);
        player2Score.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        panel.add(player1Score);
        panel.add(player2Score);
        return panel;
    }

    private void updateScoreboardLabels() {
        player1Score.setText(player1 + " - Wins: " + scoreboard.getWins(player1) + " Losses: " + scoreboard.getLosses(player1));
        player2Score.setText(player2 + " - Wins: " + scoreboard.getWins(player2) + " Losses: " + scoreboard.getLosses(player2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (!clicked.getText().equals("")) return;

        String mark = xTurn ? "X" : "O";
        clicked.setText(mark);
        int[] pos = getButtonPosition(clicked);
        board.setCell(mark, pos);

        int[][] winningLine = board.checkWinnerLine();
        if (winningLine != null) {
            String winner = xTurn ? player1 : player2;
            String loser = xTurn ? player2 : player1;

            highlightWinningLine(winningLine);
            JOptionPane.showMessageDialog(this, winner + " wins!");
            scoreboard.addWin(winner);
            scoreboard.addLoss(loser);
            scoreboard.save();
            updateScoreboardLabels();
            resetBoardDelayed();
        } else if (board.isFull()) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
            resetBoardDelayed();
        } else {
            xTurn = !xTurn;
        }
    }

    private void highlightWinningLine(int[][] line) {
        for (int[] pos : line) {
            buttons[pos[0]][pos[1]].setBackground(Color.GREEN);
        }
    }

    private void resetBoardDelayed() {
        Timer timer = new Timer(1000, e -> resetBoard());
        timer.setRepeats(false);
        timer.start();
    }

    private int[] getButtonPosition(JButton clicked) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (buttons[i][j] == clicked) return new int[]{i, j};
        return new int[]{-1, -1};
    }

    private void resetBoard() {
        board.reset();
        for (JButton[] row : buttons)
            for (JButton b : row) {
                b.setText("");
                b.setBackground(Color.WHITE);
            }
        xTurn = true;
    }
}
