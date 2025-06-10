package gui;

import client.ClientConnection;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameWindow extends JFrame {
    private final int boardSize = 10;
    private final int SIZE = 11;
    private JButton[][] playerButtons = new JButton[boardSize][boardSize];
    private JButton[][] enemyButtons = new JButton[boardSize][boardSize];
    private ClientConnection connection;
    private JLabel statusLabel;
    private JLabel timerLabel;
    private volatile boolean gameRunning = true;


    public GameWindow(Board playerBoard) {
        setTitle("Gra w Statki – Klient");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 600);
        setResizable(false);

        try {
            // polaczenie z serwerem i przeslanie rozmieszczenia statkow
            connection = new ClientConnection("localhost", 12345);
            connection.sendPlayerShips(playerBoard.getShips());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Nie udało się połączyć z serwerem.", "Błąd", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel boardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        boardsPanel.add(createBoardPanel("Twoja plansza", playerButtons, false, playerBoard));
        boardsPanel.add(createBoardPanel("Plansza przeciwnika", enemyButtons, true, null));
        content.add(boardsPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Wybierz pole, aby strzelić.", SwingConstants.CENTER);
        timerLabel = new JLabel("Czas gry: 00:00", SwingConstants.CENTER);
        content.add(timerLabel, BorderLayout.NORTH);
        content.add(statusLabel, BorderLayout.SOUTH);

        startGameTimer();
        setContentPane(content);
        setVisible(true);
    }

    private JPanel createBoardPanel(String title, JButton[][] buttons, boolean isEnemy, Board board) {
        JPanel boardPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        boardPanel.add(label, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        String[] letters = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        // tworzenie siatki planszy
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (row == 0 && col == 0) {
                    gridPanel.add(new JLabel(""));
                } else if (row == 0) {
                    JLabel colLabel = new JLabel(letters[col], SwingConstants.CENTER);
                    gridPanel.add(colLabel);
                } else if (col == 0) {
                    JLabel rowLabel = new JLabel(String.valueOf(row), SwingConstants.CENTER);
                    gridPanel.add(rowLabel);
                } else {
                    JButton button = new JButton();
                    button.setPreferredSize(new Dimension(40, 40));
                    int r = row - 1;
                    int c = col - 1;

                    if (isEnemy) {
                        button.addActionListener(e -> handlePlayerShot(r, c, button));
                    } else {
                        CellState state = board.getCellState(new Coordinate(r, c));
                        updateButtonAppearance(button, state);
                        button.setEnabled(false);
                    }

                    buttons[r][c] = button;
                    gridPanel.add(button);
                }
            }
        }

        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        container.add(gridPanel);
        boardPanel.add(container, BorderLayout.CENTER);
        return boardPanel;
    }

    // strzał gracza w dane pole planszy przeciwnika
    private void handlePlayerShot(int row, int col, JButton button) {
        try {
            String response = connection.sendShot(row, col); // wysłanie strzału

            if (response.startsWith("END")) {
                // zwyciestwo gracza
                button.setBackground(Color.RED);
                button.setEnabled(false);
                statusLabel.setText("Wygrałeś!");
                gameRunning = false;
                disableAllEnemyButtons();
                connection.close();
                saveResultToFile("Gracz wygrał");
                return;
            }

            String[] parts = response.split("\\|");

            boolean isLose = parts[0].equals("LOSE");

            boolean playerHit = parts[0].equals("HIT") || parts[0].equals("SUNK");
            boolean playerSankShip = parts[0].equals("SUNK");

            Coordinate aiCoord = parseCoordinate(parts[1]);
            boolean aiHit = parts[2].equals("HIT");
            boolean aiSankShip = parts.length > 3 && parts[3].equals("SUNK");

            button.setBackground(playerHit ? Color.RED : Color.BLUE);
            button.setEnabled(false);

            if (aiCoord != null) {
                JButton target = playerButtons[aiCoord.getRow()][aiCoord.getCol()];
                Color currentColor = target.getBackground();
                if (aiHit) {
                    target.setBackground(Color.RED);
                } else if (!Color.RED.equals(currentColor)) {
                    target.setBackground(Color.BLUE);
                }
            }

            if (playerSankShip && aiSankShip) {
                statusLabel.setText("Zatopiłeś statek! Ale przeciwnik też zatopił.");
            } else if (playerSankShip) {
                statusLabel.setText("Zatopiłeś statek przeciwnika!");
            } else if (aiSankShip) {
                statusLabel.setText("Przeciwnik zatopił Twój statek!");
            } else if (playerHit && aiHit) {
                statusLabel.setText("Trafiłeś! Ale przeciwnik też trafił.");
            } else if (playerHit) {
                statusLabel.setText("Trafiłeś! Przeciwnik spudłował.");
            } else if (aiHit) {
                statusLabel.setText("Spudłowałeś. Przeciwnik trafił!");
            } else {
                statusLabel.setText("Pudło");
            }

            // gracz przegrał
            if (isLose) {
                statusLabel.setText("Przegrałeś!");
                disableAllEnemyButtons();
                gameRunning = false;
                connection.close();
                saveResultToFile("Komputer wygrał");
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

    // zapisuje wynik gry do pliku wynik.txt
    private void saveResultToFile(String result) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("wynik.txt", true))) {
            String timestamp = java.time.LocalDateTime.now().toString();
            writer.println(timestamp + " – " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ustawia licznik czasu gry w osobnym wątku
    private void startGameTimer() {
        new Thread(() -> {
            int seconds = 0;
            while (gameRunning) {
                int mins = seconds / 60;
                int secs = seconds % 60;
                String time = String.format("Czas gry: %02d:%02d", mins, secs);
                SwingUtilities.invokeLater(() -> timerLabel.setText(time));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seconds++;
            }
        }).start();
    }


}
