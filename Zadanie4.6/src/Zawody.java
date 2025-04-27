
class Pula {
    int liczba_talerzy;
    public Pula(int liczba_talerzy) {
        this.liczba_talerzy = liczba_talerzy;
    }
}

class Pomywacz extends Thread {

    static int liczba_pomywaczy = 3;
    static Pula sterta = new Pula(30);
    int numer;
    static int wyniki[] = new int[liczba_pomywaczy];

    Pomywacz(int i){
        numer = i;
    }

    public void run() {
        while(sterta.liczba_talerzy > 0){
            sterta.liczba_talerzy--;
            try {
                sleep(200 + (long)(Math.random() * 600));
            } catch (Exception e) { System.out.println(e); }
            wyniki[numer]++;
            for(int i = 0; i < liczba_pomywaczy; i++) {
                System.out.print("P" + (i + 1) + ": " + wyniki[i] + " | ");
            }
            System.out.println();
        }
        System.out.println("Pomywacz nr " + numer + " Zakończył pracę");
    }
}

public class Zawody{
    public static void main(String[] args){
        for(int i = 0; i < Pomywacz.liczba_pomywaczy; i++){
            Pomywacz p = new Pomywacz(i);
            p.start();
        }
    }
}