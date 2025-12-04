package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Show main menu with "Start Game" button
            JFrame frame = new JFrame("Tic-Tac-Toe");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(null);

            JButton startButton = new JButton("Start Game");
            startButton.setBounds(120, 100, 150, 50);
            startButton.setFont(startButton.getFont().deriveFont(18f));

            startButton.addActionListener(e -> {
                frame.dispose(); // close menu
                String player1 = JOptionPane.showInputDialog(null, "Enter Player 1 nickname:", "Tic-Tac-Toe", JOptionPane.QUESTION_MESSAGE);
                if (player1 == null || player1.isBlank()) player1 = "Player1";

                String player2 = JOptionPane.showInputDialog(null, "Enter Player 2 nickname:", "Tic-Tac-Toe", JOptionPane.QUESTION_MESSAGE);
                if (player2 == null || player2.isBlank()) player2 = "Player2";

                TicTacToeGame game = new TicTacToeGame(player1, player2);
                game.setTitle("Tic-Tac-Toe");
                game.setSize(500, 600);
                game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                game.setVisible(true);
            });

            frame.add(startButton);
            frame.setVisible(true);
        });
    }
}
