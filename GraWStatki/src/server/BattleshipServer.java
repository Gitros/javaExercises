package server;

import java.awt.*;
import java.io.*;
import java.net.*;
import model.ComputerPlayer;

public class BattleshipServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Serwer uruchomiony...");

        Socket clientSocket = serverSocket.accept();
        System.out.println("Połączono z klientem");

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // Plansza komputera (wroga) – do obrony
        ComputerPlayer computer = new ComputerPlayer();

        // Plansza gracza – komputer na nią strzela
        char[][] playerBoard = new char[10][10];
        initializePlayerBoard(playerBoard); // zakładamy, że statki już tam są

        // AI komputera
        ComputerAI ai = new ComputerAI(playerBoard);

        while (true) {
            String line = in.readLine();
            if (line == null) break;
            System.out.println("Klient: " + line);

            if (line.startsWith("FIRE")) {
                int x = Integer.parseInt(line.split(" ")[1]);
                int y = Integer.parseInt(line.split(" ")[2]);

                // Gracz strzela do planszy komputera
                boolean playerHit = computer.getBoard().fireAt(x, y);
                out.println(playerHit ? "HIT" : "MISS");

                // Komputer strzela do planszy gracza
                Point shot = ai.getNextMove();
                boolean computerHit = (playerBoard[shot.y][shot.x] == 'S');
                playerBoard[shot.y][shot.x] = computerHit ? 'X' : '-';

                ai.reportHit(shot, computerHit);

                // Wysyłanie strzału komputera do klienta
                out.println("COMPUTER_FIRE " + shot.x + " " + shot.y);
                out.println(computerHit ? "COMPUTER_HIT" : "COMPUTER_MISS");
            }
        }

        clientSocket.close();
        serverSocket.close();
    }

    private static void initializePlayerBoard(char[][] board) {
        // Domyślnie wszystko puste
        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                board[y][x] = 'O';

        // Na potrzeby testów ustawiamy kilka statków gracza ręcznie
        // Można to później zmienić na odbieranie danych z klienta
        placeShip(board, 0, 0, 5, true);  // statek poziomy
        placeShip(board, 2, 2, 4, false); // pionowy
        placeShip(board, 5, 5, 3, true);
        placeShip(board, 7, 1, 3, false);
        placeShip(board, 8, 6, 2, true);
    }

    private static void placeShip(char[][] board, int x, int y, int size, boolean horizontal) {
        for (int i = 0; i < size; i++) {
            if (horizontal) {
                board[y][x + i] = 'S';
            } else {
                board[y + i][x] = 'S';
            }
        }
    }
}
