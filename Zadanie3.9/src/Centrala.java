import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
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
    public ArrayList<Abonent> m_abonenci = new ArrayList<Abonent>();
    // obiekt widoku listy
    public JList m_lista = new JList();

    public ListaAbonentow() {

        setLayout(new BorderLayout());

        // do napisania (1)
        // wyposażenie listy w obramowanie i możliwość scrollowania
        // i umieszczenie jej w panelu


        // dane do testów
        m_abonenci.add(new Abonent(new String[]{"Jan", "Kowalski","111","222","333","444","555"}));
        m_abonenci.add(new Abonent(new String[]{"Agata", "Kowalska","1111","2222","3333","4444","5555"}));
        m_lista.setListData(m_abonenci.toArray());

        JScrollPane scroll = new JScrollPane(m_lista);
        add(scroll, BorderLayout.CENTER);
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

        int wybranyIndex = m_lista.getSelectedIndex();
        if(wybranyIndex == -1) {
            m_abonenci.add(new Abonent(dane));
        } else {
            for (int i = 0; i < Abonent.ETYKIETY.length; i++) {
                m_abonenci.get(wybranyIndex).set(i, dane[i]);
            }
        }
        m_lista.setListData(m_abonenci.toArray());
    }

    public void usun(int i) {
        // do napisania (7.2)
        /*
         * Usuwa z kolekcji i odświeża listę (w tym zaznaczenie elementu)
         */
        if(i >= 0 && i < m_abonenci.size()) {
            m_abonenci.remove(i);
            m_lista.setListData(m_abonenci.toArray());
        }
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

        panel = new PanelAbonenta(c);
        add(panel);

        zatwierdz = new JButton("Zatwierdz");
        zatwierdz.addActionListener(this);
        add(zatwierdz);

    }

    public void actionPerformed(ActionEvent e) {
        // póki co tylko obsługa zatwierdzenia zmian
        // (reakcja na wcisnięcie przycisku "Zatwierdź")
        /*
        do napisania obsługa zatwierdzenia zmian (6.1)
        */
        zapiszZmiany();
        dispose();

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

    private JMenuItem zamknij, dodaj, edytuj, usun, zapiszListe, odczytajListe;

    public Centrala()
    {
        super("Lista abonentow");

        JPanel glowny = new JPanel(new GridLayout(1,1,5,5));
        glowny.add(m_listaAbonentow);
        add(glowny);
        // do napisania (2)
        /* zbudowanie odpowiedniego menu */

        JMenuBar menuBar = new JMenuBar();

        JMenu menuProgram = new JMenu("Program");
        zamknij = new JMenuItem("Zamknij");
        zamknij.addActionListener(this);
        menuProgram.add(zamknij);

        JMenu menuOperacje = new JMenu("Operacje");

        dodaj = new JMenuItem("Dodaj abonenta");
        dodaj.addActionListener(this);
        menuOperacje.add(dodaj);

        edytuj = new JMenuItem("Edytuj abonenta");
        edytuj.addActionListener(this);
        menuOperacje.add(edytuj);

        usun = new JMenuItem("Usun abonenta");
        usun.addActionListener(this);
        menuOperacje.add(usun);

        JMenu menuDane = new JMenu("Dane");

        zapiszListe = new JMenuItem("Zapisz listę abonentów");
        zapiszListe.addActionListener(this);
        menuDane.add(zapiszListe);

        odczytajListe = new JMenuItem("Wczytaj listę abonentów");
        odczytajListe.addActionListener(this);
        menuDane.add(odczytajListe);

        menuBar.add(menuProgram);
        menuBar.add(menuOperacje);
        menuBar.add(menuDane);

        setJMenuBar(menuBar);

        setSize(400,300);
        setLocation(50, 100);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        odczytajListe();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                zapiszListe();
                System.exit(0);
            }
        });

        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK), "odczytajListe");
        actionMap.put("odczytajListe", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                odczytajListe();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK), "zapiszListe");
        actionMap.put("zapiszListe", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                zapiszListe();
            }
        });
    }

    private void zapiszListe() {
        File plik = new File("abonenci.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(plik))) {
            writer.write(String.join(",", Abonent.ETYKIETY) + "\n");
            for (Abonent abonent : m_listaAbonentow.m_abonenci) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < Abonent.ETYKIETY.length; i++) {
                    line.append(abonent.get(i)).append(i < Abonent.ETYKIETY.length - 1 ? "," : "");
                }
                line.append("\n");
                writer.write(line.toString());
            }
            JOptionPane.showMessageDialog(this, "Dane zostały zapisane.", "Zapisano", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Błąd zapisu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void odczytajListe() {
        File plik = new File("abonenci.txt");
        if (plik.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(plik))) {
                String line;
                reader.readLine();

                ArrayList<Abonent> abonenci = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] dane = line.split(",");
                    abonenci.add(new Abonent(dane));
                }
                m_listaAbonentow.m_abonenci = abonenci;
                m_listaAbonentow.m_lista.setListData(abonenci.toArray());

                JOptionPane.showMessageDialog(this, "Lista abonentów została załadowana.", "Załadowano", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd odczytu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Plik nie istnieje.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        // do napisania obsługa menu:
        // polecenia Dodaj Abonenta (3)
        if (e.getSource() == dodaj) {
            m_listaAbonentow.setIndeksAbonenta(-1);
            OknoAbonenta okno = new OknoAbonenta(this);
            okno.setVisible(true);
        }
        // polecenia Zamknij (8)
        if(e.getSource() == zamknij) {
            System.exit(0);
        }
        // polecenia Edytuj (5)
        if(e.getSource() == edytuj) {
            int index = m_listaAbonentow.getIndeksAbonenta();
            if(index != -1) {
                OknoAbonenta okno = new OknoAbonenta(this);
                okno.setEdytowany(m_listaAbonentow.getAbonent());
                okno.setVisible(true);
            }
        }
        // polecenia Usun (7.1) oraz (7.2) implementacja metody usun() z klasy ListaAbonentow
        if(e.getSource() == usun) {
            int index = m_listaAbonentow.getIndeksAbonenta();
            if(index != -1) {
                m_listaAbonentow.usun(index);
            }
        }
        if(e.getSource() == zapiszListe) {
            zapiszListe();
        }
        if(e.getSource() == odczytajListe) {
            odczytajListe();
        }
    }

    public ListaAbonentow getListaAbonentow() {
        return m_listaAbonentow;
    }

    public static void main(String[] args)
    {
        new Centrala();
    }

}