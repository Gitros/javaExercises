import java.io.*;
import java.net.*;
public class Server
{
    public static final int serverPort = 2020;
    ServerSocket s;

    /* Konstruktor próbuje utworzyć gniazdo */
    Server()
    {
        try{
            s = new ServerSocket(serverPort);
            System.out.println("Serwer uruchomiony");
        }catch(Exception e) {
            System.out.println("Nie można utworzyć gniazda");
            System.exit(1);
        }
    }

    void dzialaj()
    {
        /* Deklaracje potrzebnych zmiennych */
        Socket socket = null;
        try {
            /* Czekaj, aż klient się połączy */
            socket = s.accept();
            /* Czynności przygotowawcze do obsługi klienta */

 /* 1. Otworzenie strumienia (wejściowego lub wyjściowego) i
 /* skojarzenie go z gniazdem klienta */

            while(true)
            {
                if(true)
                {
                    System.out.println("Koniec pracy serwera");
                    break;
                }
                else
                {
                    /* Wyślij/Odbierz dane do/od klienta */
                    /* (do/ze strumienia związanego z gniazdem) */

                }
            }//koniec pętli while(true)
        }
        catch(Exception e) {
            System.out.println("Problem w komunikacji z klientem");
        }
        finally {
            try {
                /* zamykanie połączenia */
                s.close();
            } catch(Exception e) {
                System.out.println("Problem z zamykaniem połączenia");
            }
        }
    }//koniec funkcji dzialaj()

    public static void main(String args[])
    {
        Server server = new Server();
        server.dzialaj();
        try { server.s.close(); }
        catch (IOException e) { System.out.println(e); }
    }
} 