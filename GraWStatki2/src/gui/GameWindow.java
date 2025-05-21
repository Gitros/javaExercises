package gui;

import client.ClientConnection;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameWindow extends JFrame {

    private final int boardSize = 10;
    private JButton[][] playerButtons = new JButton[boardSize][boardSize];
    private JButton[][] enemyButtons = new JButton[boardSize][boardSize];
    private ClientConnection connection;
    private JLabel statusLabel;

    public GameWindow(Board playerBoard) {
        setTitle("Gra w Statki – Klient");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            connection = new ClientConnection("localhost", 12345); // 🔌 Połączenie z serwerem
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Nie udało się połączyć z serwerem.", "Błąd", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Twój panel
        JPanel playerPanel = createBoardPanel(playerButtons, false, playerBoard);
        playerPanel.setBorder(BorderFactory.createTitledBorder("Twoja plansza"));

        // Panel przeciwnika
        JPanel enemyPanel = createBoardPanel(enemyButtons, true, null);
        enemyPanel.setBorder(BorderFactory.createTitledBorder("Plansza przeciwnika"));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(playerPanel);
        centerPanel.add(enemyPanel);
        add(centerPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Wybierz pole, aby strzelić.", SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createBoardPanel(JButton[][] buttons, boolean isEnemy, Board board) {
        JPanel panel = new JPanel(new GridLayout(boardSize, boardSize));
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(40, 40));
                int r = row;
                int c = col;

                if (isEnemy) {
                    button.addActionListener(e -> handlePlayerShot(r, c, button));
                } else {
                    CellState state = board.getCellState(new Coordinate(r, c));
                    updateButtonAppearance(button, state);
                    button.setEnabled(false);
                }

                buttons[row][col] = button;
                panel.add(button);
            }
        }
        return panel;
    }

    private void handlePlayerShot(int row, int col, JButton button) {
        try {
            String response = connection.sendShot(row, col);
            String[] parts = response.split("\\|");

            if (parts[0].equals("END")) {
                button.setBackground(Color.RED);
                statusLabel.setText("🎉 Wygrałeś!");
                disableAllEnemyButtons();
                connection.close();
                return;
            }

            if (parts[0].equals("LOSE")) {
                Coordinate aiCoord = parseCoordinate(parts[1]);
                JButton aiButton = playerButtons[aiCoord.getRow()][aiCoord.getCol()];
                aiButton.setBackground(Color.RED);
                statusLabel.setText("💀 Przegrałeś!");
                disableAllEnemyButtons();
                connection.close();
                return;
            }

            // 1. Twoja odpowiedź na strzał
            button.setBackground(parts[0].equals("HIT") ? Color.RED : Color.BLUE);
            button.setEnabled(false);

            // 2. AI strzela
            Coordinate aiShot = parseCoordinate(parts[1]);
            JButton target = playerButtons[aiShot.getRow()][aiShot.getCol()];
            if (parts[2].equals("HIT")) {
                target.setBackground(Color.RED);
                statusLabel.setText("Trafiony + przeciwnik trafił!");
            } else {
                target.setBackground(Color.BLUE);
                statusLabel.setText("Trafiony + przeciwnik spudłował.");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Błąd komunikacji z serwerem.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Coordinate parseCoordinate(String coordStr) {
        String[] parts = coordStr.split(",");
        return new Coordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }


    private void disableAllEnemyButtons() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                enemyButtons[row][col].setEnabled(false);
            }
        }
    }

    private void updateButtonAppearance(JButton button, CellState state) {
        switch (state) {
            case SHIP -> button.setBackground(Color.GRAY);
            case HIT -> button.setBackground(Color.RED);
            case MISS -> button.setBackground(Color.BLUE);
            default -> button.setBackground(null);
        }
    }
}
