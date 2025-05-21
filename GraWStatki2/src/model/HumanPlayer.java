package model;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, int boardSize) {
        super(name, boardSize);
    }

    @Override
    public Coordinate makeMove() {
        // ⏳ Na razie proste odczytanie z konsoli,
        // a gdy dodasz Swing – koordynaty z kliknięcia myszy.
        Scanner sc = new Scanner(System.in);
        System.out.print("Podaj strzał (r c): ");
        int r = sc.nextInt();
        int c = sc.nextInt();
        return new Coordinate(r, c);
    }

    @Override
    public void placeShips(List<Ship> fleet) {
        // Wersja konsolowa albo później GUI – Twój wybór.
    }
}
