package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI extends JFrame {
    private JButton[][] enemyButtons = new JButton[10][10];
    private JLabel statusLabel = new JLabel("Twoja tura!");
    private PrintWriter out;
    private BufferedReader in;
    private boolean setupPhase = true;
    private int[] shipSizes = {5, 4, 3, 3, 2}; // klasyczne statki
    private int currentShipIndex = 0;
    private boolean horizontal = true;


    public ClientGUI() {
        setTitle("Statki - Klient");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(createPlayerBoard());     // Później dodamy logikę
        mainPanel.add(createEnemyBoard());      // Plansza przeciwnika (klikalna)

        add(mainPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        connectToServer();
    }

    private JButton[][] playerBoardButtons = new JButton[10][10];

    private JPanel createPlayerBoard() {
        JPanel panel = new JPanel(new GridLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Twoja plansza"));

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                final int fx = x;
                final int fy = y;
                JButton btn = new JButton();
                playerBoardButtons[y][x] = btn;

                btn.addActionListener(e -> {
                    if (setupPhase) {
                        placeShipOnPlayerBoard(fx, fy);
                    }
                });

                panel.add(btn);
            }
        }

        return panel;
    }

    private void placeShipOnPlayerBoard(int x, int y) {
        int size = shipSizes[currentShipIndex];

        if (canPlaceShip(playerBoardButtons, x, y, size, horizontal)) {
            for (int i = 0; i < size; i++) {
                if (horizontal) playerBoardButtons[y][x + i].setBackground(Color.GRAY);
                else playerBoardButtons[y + i][x].setBackground(Color.GRAY);
            }

            currentShipIndex++;
            if (currentShipIndex >= shipSizes.length) {
                setupPhase = false;
                statusLabel.setText("Statki ustawione. Gra się zaczyna!");
            } else {
                statusLabel.setText("Ustaw statek długości " + shipSizes[currentShipIndex]);
            }
        } else {
            statusLabel.setText("Nieprawidłowe ustawienie statku!");
        }
    }

    private boolean canPlaceShip(JButton[][] board, int x, int y, int size, boolean horizontal) {
        try {
            if (horizontal) {
                if (x + size > 10) return false;
                for (int i = 0; i < size; i++) {
                    if (!board[y][x + i].getBackground().equals(new JButton().getBackground()))
                        return false;
                }
            } else {
                if (y + size > 10) return false;
                for (int i = 0; i < size; i++) {
                    if (!board[y + i][x].getBackground().equals(new JButton().getBackground()))
                        return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private JPanel createEnemyBoard() {
        JPanel panel = new JPanel(new GridLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Plansza przeciwnika"));
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                final int fx = x;
                final int fy = y;
                JButton btn = new JButton();
                enemyButtons[y][x] = btn;

                btn.addActionListener(e -> {
                    out.println("FIRE " + fx + " " + fy);
                    try {
                        String response = in.readLine();
                        if (response.equals("HIT")) {
                            btn.setText("X");
                            btn.setBackground(Color.RED);
                            statusLabel.setText("Trafiony!");
                        } else if (response.equals("MISS")) {
                            btn.setText("•");
                            btn.setBackground(Color.BLUE);
                            statusLabel.setText("Pudło!");
                        }
                        btn.setEnabled(false); // nie można klikać dwa razy
                    } catch (IOException ex) {
                        statusLabel.setText("Błąd komunikacji z serwerem.");
                        ex.printStackTrace();
                    }
                });

                panel.add(btn);
            }
        }
        return panel;
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            statusLabel.setText("Połączono z serwerem. Twoja tura!");
        } catch (IOException e) {
            statusLabel.setText("Nie udało się połączyć z serwerem.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientGUI().setVisible(true);
        });
    }
}
