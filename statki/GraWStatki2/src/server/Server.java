package server;

import model.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private final int boardSize = 10;
    private final ComputerPlayer ai = new ComputerPlayer("AI", boardSize);
    private final List<Ship> fleet;
    private Board playerBoard;

    // Konstruktor tworzy domyślną flotę i ustawia ją dla AI
    public Server() {
        fleet = List.of(
                new Ship(Collections.nCopies(4, new Coordinate(0, 0))),
                new Ship(Collections.nCopies(3, new Coordinate(0, 0))),
                new Ship(Collections.nCopies(2, new Coordinate(0, 0))),
                new Ship(Collections.nCopies(1, new Coordinate(0, 0)))
        );
        ai.placeShips(cloneFleet(fleet));
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serwer nasłuchuje na porcie " + PORT + "...");

            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                System.out.println("Połączono z klientem: " + clientSocket.getInetAddress());

                // Odbieranie statków gracza od klienta w formacie tekstowym
                List<Ship> playerShips = new ArrayList<>();
                String shipLine;
                while ((shipLine = in.readLine()) != null && !shipLine.equals("READY")) {
                    playerShips.add(parseShip(shipLine));
                }

                // Inicjalizacja planszy gracza i rozmieszczenie jego statków
                playerBoard = new Board(boardSize);
                for (Ship ship : playerShips) {
                    playerBoard.placeShip(ship);
                }

                // Główny strumień gry
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("END")) break;

                    String[] parts = inputLine.split(",");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    Coordinate coord = new Coordinate(row, col);

                    boolean hit = ai.getOwnBoard().receiveShot(coord);
                    boolean shipSunk = ai.getOwnBoard().wasLastShipSunk();
                    boolean aiDefeated = ai.getOwnBoard().allShipsSunk();

                    String result = hit ? (shipSunk ? "SUNK" : "HIT") : "MISS";
                    if (aiDefeated) {
                        out.println("END");
                        break;
                    }

                    // Strzał ai w gracza
                    Coordinate aiShot = ai.makeMove();
                    boolean aiHit = playerBoard.receiveShot(aiShot);
                    boolean aiSankShip = playerBoard.wasLastShipSunk();
                    ai.handleShotResult(aiShot, aiHit);

                    boolean playerDefeated = playerBoard.allShipsSunk();
                    String response;

                    if (playerDefeated) {
                        response = "LOSE|" + aiShot.getRow() + "," + aiShot.getCol() + "|HIT";
                    } else {
                        response = String.format("%s|%d,%d|%s|%s",
                                result,
                                aiShot.getRow(), aiShot.getCol(),
                                aiHit ? "HIT" : "MISS",
                                aiSankShip ? "SUNK" : "NOSINK"
                        );
                    }

                    out.println(response);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Tworzy głęboką kopię floty (kopie statków z kopiami pozycji)
    private List<Ship> cloneFleet(List<Ship> original) {
        List<Ship> copy = new ArrayList<>();
        for (Ship s : original) {
            copy.add(new Ship(new ArrayList<>(s.getPositions())));
        }
        return copy;
    }

    private Ship parseShip(String data) {
        String[] parts = data.split(";");
        List<Coordinate> coords = new ArrayList<>();
        for (String part : parts) {
            if (!part.isEmpty()) {
                String[] rc = part.split(",");
                coords.add(new Coordinate(Integer.parseInt(rc[0]), Integer.parseInt(rc[1])));
            }
        }
        return new Ship(coords);
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
