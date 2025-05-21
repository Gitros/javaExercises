package gui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SetupWindow extends JFrame {
    private final int boardSize = 10;
    private JButton[][] buttons = new JButton[boardSize][boardSize];
    private Board playerBoard = new Board(boardSize);
    private JLabel statusLabel;
    private int currentShipSize = 3;
    private List<Integer> shipsToPlace = List.of(3, 2, 1); // przykładowa flota
    private int shipIndex = 0;
    private boolean horizontal = true;

    public SetupWindow() {
        setTitle("Rozmieszczanie statków");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(40, 40));
                int row = r, col = c;
                btn.addActionListener(e -> tryPlaceShip(row, col));
                buttons[r][c] = btn;
                boardPanel.add(btn);
            }
        }

        // Przyciski dolne
        JPanel bottomPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Ustaw statek długości 3", SwingConstants.CENTER);
        JButton rotateBtn = new JButton("Obróć (H/V)");
        rotateBtn.addActionListener(e -> {
            horizontal = !horizontal;
            rotateBtn.setText(horizontal ? "Obróć (H)" : "Obróć (V)");
        });

        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(rotateBtn, BorderLayout.EAST);

        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void tryPlaceShip(int row, int col) {
        int length = shipsToPlace.get(shipIndex);
        List<Coordinate> coords = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            if (r >= boardSize || c >= boardSize) {
                statusLabel.setText("❌ Poza planszą");
                return;
            }
            coords.add(new Coordinate(r, c));
        }

        Ship ship = new Ship(coords);
        if (playerBoard.placeShip(ship)) {
            for (Coordinate coord : coords) {
                buttons[coord.getRow()][coord.getCol()].setBackground(Color.GRAY);
                buttons[coord.getRow()][coord.getCol()].setEnabled(false);
            }

            shipIndex++;
            if (shipIndex >= shipsToPlace.size()) {
                statusLabel.setText("✅ Wszystkie statki ustawione!");
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    new GameWindow(playerBoard); // Start gry z ustawioną planszą
                });
            } else {
                currentShipSize = shipsToPlace.get(shipIndex);
                statusLabel.setText("Ustaw statek długości " + currentShipSize);
            }
        } else {
            statusLabel.setText("❌ Kolizja z innym statkiem");
        }
    }
}
