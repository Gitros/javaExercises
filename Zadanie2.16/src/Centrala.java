package zad_II_16_0;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;

// klasa reprezentujaca jednego abonenta
class Abonent {

    public final static String[] ETYKIETY = {
        "Imie",
        "Nazwisko",
        "Numer telefonu",
        "Ilosc darmowych minut",
        "Oplata abonentowa",
        "Cena za minute w sieci",
        "Cena za minute poza siecia"
    };
    private HashMap<String,String> m_data = new HashMap<String,String>();

    public Abonent(String[] tablicaDanych) {
        for(int i=0;i<ETYKIETY.length;i++)
            m_data.put(ETYKIETY[i],tablicaDanych[i]);
    }

    // funkcja zwracajaca jedna z wlasciwosci abonenta
    public String get(int numerPola) {
        return m_data.get(ETYKIETY[numerPola]);
    }

    // funkcja uaktualniajaca jedna z wlasciwosci abonenta
    public void set(int numerPola,String a_nowaWartosc) {
        m_data.put(ETYKIETY[numerPola],a_nowaWartosc);
    }

    public String toString() {
        return get(0) + " " + get(1);
    }    
}

// --------------------------------------------------------------------

// klasa panelu wyswietlania/wprowadzania nowego abonenta
// panel umieścimy w oknie klasy OknoAbonenta
class PanelAbonenta extends JPanel {
   
    // tablica pól tekstowych do edycji danych abonenta
    private JTextField[] m_pola = new JTextField[Abonent.ETYKIETY.length];

    public PanelAbonenta(Centrala c) {
        // ustawia layout, siatka liczbaPól x 2 z odstepami po 5 w pionie i poziomie
        setLayout(new GridLayout(Abonent.ETYKIETY.length,2,5,5));

        // tworzy i dodaje do panelu pola tekstowe
        for(int i=0;i<Abonent.ETYKIETY.length;i++) {
            add(new JLabel(Abonent.ETYKIETY[i]));
            m_pola[i] = new JTextField();
            add(m_pola[i]);
        }

        // definiuje obramowanie
        TitledBorder border = new TitledBorder("Dane abonenta");
        border.setTitleColor(Color.RED);
        setBorder(border);        
    }
    
    public String getPole(int i) { return m_pola[i].getText(); }
    public void setPole(int i, String value) { m_pola[i].setText(value); }
}
// --------------------------------------------------------------------

// klasa reprezentująca panel wyswietlający listę abonentow
class ListaAbonentow extends JPanel
{
    // kontener z listą abonentów
    private ArrayList<Abonent> m_abonenci = new ArrayList<Abonent>();
    // obiekt widoku listy
    private JList m_lista = new JList();    

    public ListaAbonentow() {        

        setLayout(new BorderLayout());        

     // do napisania (1)
     // wyposażenie listy w obramowanie i możliwość scrollowania           
     // i umieszczenie jej w panelu  
     
     
     // dane do testów
        m_abonenci.add(new Abonent(new String[]{"Jan", "Kowalski","111","222","333","444","555"}));
        m_abonenci.add(new Abonent(new String[]{"Agata", "Kowalska","1111","2222","3333","4444","5555"}));
        m_lista.setListData(m_abonenci.toArray());        
    }        

    public void zapiszZmiany(String[] dane) {
        // do napisania (6.2)
        /*
         * W zależności od wybranej pozycji w liście
         * aktualizacja lub dodanie elementu do kolekcji związanej z listą.
         * Odświeżenie widoku listy (w tym wybranego elementu)
         * Należy wykorzystać metody klas ArrayList oraz Abonent.
         * 
        */
        
    }

    public void usun(int i) {
      // do napisania (7.2)
        /*
         * Usuwa z kolekcji i odświeża listę (w tym zaznaczenie elementu)
        */
        
    }

    // zwraca indeks elementu wybranego w liście
    public int getIndeksAbonenta() { return m_lista.getSelectedIndex(); }
    
    public void setIndeksAbonenta(int i) { 
        if(i >= 0) m_lista.setSelectedIndex(i); 
        else m_lista.clearSelection();
    }
    
    public Abonent getAbonent() { return m_abonenci.get(m_lista.getSelectedIndex()); }
    
}
// --------------------------------------------------------------------
// klasa reprezentująca okno dialogowe
// wewnątrz którego będzie panel edycji abonenta
class OknoAbonenta extends JDialog implements ActionListener { 

    private Centrala centrala;
    private PanelAbonenta panel;
    private JButton zatwierdz;

    public OknoAbonenta(Centrala c) {
        centrala = c;
        
        setSize(400, 280);
        setLocation(50, 100);
        setLayout(new FlowLayout());
        setModal(true);
        
        /* do napisania (4):
         * wstawić panel abonenta i przycisk "Zatwierdź"
        */
        
    }
        
    public void actionPerformed(ActionEvent e) {
      // póki co tylko obsługa zatwierdzenia zmian
      // (reakcja na wcisnięcie przycisku "Zatwierdź")
        /*
        do napisania obsługa zatwierdzenia zmian (6.1)
        */
        
    }

    // zapisuje wartości z panelu do listy abonentów
    public void zapiszZmiany() {
        String[] dane = new String[Abonent.ETYKIETY.length];
        for(int i=0; i<dane.length; i++) dane[i] = panel.getPole(i);
        ListaAbonentow abonenci = centrala.getListaAbonentow();
        abonenci.zapiszZmiany(dane);
    }

    // ustawia w polach panelu dane podanego abonenta
    public void setEdytowany(Abonent abonent) {
        for(int i=0; i<Abonent.ETYKIETY.length; i++) panel.setPole(i,abonent.get(i));
    }
        
}

class Centrala extends JFrame implements ActionListener {

    // kontener z listą abonentów
    private ArrayList<Abonent> m_abonenci = new ArrayList<>();
    private OknoAbonenta m_oknoAbonenta = new OknoAbonenta(this);
    private ListaAbonentow m_listaAbonentow = new ListaAbonentow();

    private JMenuItem zamknij, dodaj, edytuj, usun;
    
    public Centrala()
    {
        super("Lista abonentow");        

        JPanel glowny = new JPanel(new GridLayout(1,1,5,5));
        glowny.add(m_listaAbonentow);
        add(glowny);
      // do napisania (2)
        /* zbudowanie odpowiedniego menu */
        
        
        setSize(400,300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e)
    {
        // do napisania obsługa menu:
        // polecenia Dodaj Abonenta (3)
        // polecenia Zamknij (8)
        // polecenia Edytuj (5)
        // polecenia Usun (7.1) oraz (7.2) implementacja metody usun() z klasy ListaAbonentow
    }

    public ListaAbonentow getListaAbonentow() {
        return m_listaAbonentow;
    }    
    
    public static void main(String[] args)
    {
        new Centrala();
    }        
    
}