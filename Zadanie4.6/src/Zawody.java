class Pomywacz extends Thread {

    // liczba zawodników – mogłaby być zdefiniowana również w klasie Zawody
    static int liczba_pomywaczy = 3;

    // liczba talerzy do umycia - identyczna dla każdego zawodnika
    static int liczba_talerzy = 20;

    int numer; // numer zawodnika
    int umytych; // liczba już umytych talerzy

    // konstruktor
    Pomywacz(int i){
        // nadanie początkowych wartości zmiennym obiektu
        numer = i;
        umytych = 0;
    }

    public void run() {
        // odpowiednia pętla:
        while(umytych < liczba_talerzy){
            // 1) zwiększenie licznika umytych,
            umytych++;
            // 2) losowa przerwa w działaniu,
            try {
                sleep(200 + (long)(Math.random() * 600));
            } catch (Exception e) { System.out.println(e); }
            // 3) wypisanie tekstu o umyciu
            System.out.println("Pomywacz nr " + numer + "umył talerz " + umytych);
        }
        System.out.println("Pomywacz nr " + numer + " Zakończył pracę");
    }
}

public class Zawody{
    public static void main(String[] args){
        // utworzenie obiektów-wątków klasy Pomywacz i ich uruchomienie
        for(int i = 1; i <= Pomywacz.liczba_pomywaczy; i++){
            Pomywacz p = new Pomywacz(i);
            p.start();
        }
    }
}