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


    private final List<Coordinate> availableShots = new ArrayList<>();

    public Server() {
        fleet = List.of(
                new Ship(Collections.nCopies(3, new Coordinate(0, 0))),
                new Ship(Collections.nCopies(2, new Coordinate(0, 0))),
                new Ship(Collections.nCopies(1, new Coordinate(0, 0)))
        );
        ai.placeShips(cloneFleet(fleet));

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                availableShots.add(new Coordinate(r, c));
            }
        }

        // Dodaj testowego gracza z planszą
        playerBoard = new Board(boardSize);
        Ship test = new Ship(List.of(
                new Coordinate(2, 2),
                new Coordinate(2, 3),
                new Coordinate(2, 4)
        ));
        playerBoard.placeShip(test);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serwer nasłuchuje na porcie " + PORT + "...");

            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                System.out.println("Połączono z klientem: " + clientSocket.getInetAddress());

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("END")) break;

                    String[] parts = inputLine.split(",");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    Coordinate coord = new Coordinate(row, col);

                    boolean hit = ai.getOwnBoard().receiveShot(coord);
                    boolean aiDefeated = ai.getOwnBoard().allShipsSunk();

                    String result = hit ? "HIT" : "MISS";
                    if (aiDefeated) {
                        out.println("END|0,0|NONE");
                        break;
                    }

                    // AI strzela losowo
                    Coordinate aiShot = getRandomAIStrike();
                    boolean aiHit = playerBoard.receiveShot(aiShot);
                    boolean playerDefeated = playerBoard.allShipsSunk();

                    String response = String.format("%s|%d,%d|%s",
                            result, aiShot.getRow(), aiShot.getCol(), aiHit ? "HIT" : "MISS");

                    if (playerDefeated) {
                        response = "LOSE|" + aiShot.getRow() + "," + aiShot.getCol() + "|HIT";
                    }

                    out.println(response);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Ship> cloneFleet(List<Ship> original) {
        List<Ship> copy = new ArrayList<>();
        for (Ship s : original) {
            copy.add(new Ship(new ArrayList<>(s.getPositions())));
        }
        return copy;
    }

    public static void main(String[] args) {
        new Server().start();
    }

    private Coordinate getRandomAIStrike() {
        Random rand = new Random();
        int index = rand.nextInt(availableShots.size());
        return availableShots.remove(index);
    }

}
