package logic;

import model.*;

import java.util.*;

public class Game {
    private final Player human;
    private final Player computer;
    private final int boardSize = 10;
    private final Scanner scanner;

    public Game() {
        scanner = new Scanner(System.in);
        human = new HumanPlayer("Gracz", boardSize);
        computer = new ComputerPlayer("Komputer", boardSize);
    }

    public void start() {
        System.out.println("=== GRA W STATKI ===");
        List<Ship> fleet = createFleet();

        System.out.println("\nRozmieszczanie statków gracza...");
        human.placeShips(cloneFleet(fleet));

        System.out.println("Rozmieszczanie statków komputera...");
        computer.placeShips(cloneFleet(fleet));

        // 🌀 Pętla gry
        boolean gameOver = false;
        while (!gameOver) {
            System.out.println("\nTwoja plansza:");
            human.getOwnBoard().display();

            System.out.println("\nPlansza przeciwnika (co wiesz):");
            human.getOpponentViewBoard().display();

            System.out.println("\n=== RUCH GRACZA ===");
            Coordinate target = human.makeMove();
            boolean hit = computer.getOwnBoard().receiveShot(target);
            human.handleShotResult(target, hit);
            System.out.println(hit ? "Trafiony!" : "Pudło.");

            if (computer.getOwnBoard().allShipsSunk()) {
                System.out.println("\n🎉 WYGRAŁEŚ! 🎉");
                gameOver = true;
                break;
            }

            System.out.println("\n=== RUCH KOMPUTERA ===");
            Coordinate computerShot = computer.makeMove();
            boolean compHit = human.getOwnBoard().receiveShot(computerShot);
            computer.handleShotResult(computerShot, compHit);
            System.out.println("Komputer strzela w " + computerShot + ": " + (compHit ? "TRAFIONY!" : "PUDŁO."));

            if (human.getOwnBoard().allShipsSunk()) {
                System.out.println("\n💀 PRZEGRAŁEŚ 💀");
                gameOver = true;
            }
        }

        System.out.println("\n=== KONIEC GRY ===");
    }

    // 👉 Tworzy prostą flotę (1x3, 1x2, 1x1)
    private List<Ship> createFleet() {
        List<Ship> fleet = new ArrayList<>();
        fleet.add(new Ship(Collections.nCopies(3, new Coordinate(0, 0)))); // tymczasowe pozycje, zmieniane później
        fleet.add(new Ship(Collections.nCopies(2, new Coordinate(0, 0))));
        fleet.add(new Ship(Collections.nCopies(1, new Coordinate(0, 0))));
        return fleet;
    }

    // 👉 Kopiowanie floty dla każdego gracza (żeby nie dzielić tych samych obiektów)
    private List<Ship> cloneFleet(List<Ship> original) {
        List<Ship> copy = new ArrayList<>();
        for (Ship s : original) {
            copy.add(new Ship(new ArrayList<>(s.getPositions())));
        }
        return copy;
    }
}
