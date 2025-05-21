package client;

import java.io.*;
import java.net.*;

public class BattleshipClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        System.out.println("Połączono z serwerem");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("FIRE 3 4");
        String response = in.readLine();
        System.out.println("Serwer: " + response);

        socket.close();
    }
}
