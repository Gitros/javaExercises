package client;
import java.util.List;
import java.io.*;
import java.net.*;
import model.Ship;
import model.Coordinate;

public class ClientConnection {
    // gniazdo
    private Socket socket;
    // strumienie
    private PrintWriter out;
    private BufferedReader in;

    // laczenie z serwerem
    public ClientConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // wysylanie listy statkow do serwera
    public void sendPlayerShips(List<Ship> ships) {
        for (Ship ship : ships) {
            out.println(serializeShip(ship));
        }
        out.println("READY");
    }

    // zamienia statki na tekst (koordynaty np. 1,2; 1,3;)
    private String serializeShip(Ship ship) {
        StringBuilder sb = new StringBuilder();
        for (Coordinate c : ship.getPositions()) {
            sb.append(c.getRow()).append(",").append(c.getCol()).append(";");
        }
        return sb.toString();
    }

    // wysyłanie strzału do serwera
    public String sendShot(int row, int col) throws IOException {
        out.println(row + "," + col);
        String response = in.readLine();
        if (response == null) {
            throw new IOException("Brak odpowiedzi z serwera.");
        }
        return response;
    }

    // zamkniecie połączenia
    public void close() {
        try {
            out.println("END");
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
