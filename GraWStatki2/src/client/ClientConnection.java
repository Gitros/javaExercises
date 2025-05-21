package client;

import java.io.*;
import java.net.*;

public class ClientConnection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
