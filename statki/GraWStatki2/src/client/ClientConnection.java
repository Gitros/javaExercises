package client;
import java.util.List;
import java.io.*;
import java.net.*;
import model.Ship;
import model.Coordinate;

public class ClientConnection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendPlayerShips(List<Ship> ships) {
        for (Ship ship : ships) {
            out.println(serializeShip(ship));
        }
        out.println("READY"); // sygnał końca transmisji
    }

    private String serializeShip(Ship ship) {
        StringBuilder sb = new StringBuilder();
        for (Coordinate c : ship.getPositions()) {
            sb.append(c.getRow()).append(",").append(c.getCol()).append(";");
        }
        return sb.toString();
    }

    public String sendShot(int row, int col) throws IOException {
        out.println(row + "," + col);
        return in.readLine(); // HIT / MISS / END
    }

    public void close() {
        try {
            out.println("END");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
