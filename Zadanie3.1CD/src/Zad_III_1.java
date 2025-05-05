import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Zad_III_1 {
    public static void main(String[] args) {
        FileBrowser f = new FileBrowser();
        f.init();
        f.setVisible(true);
    }
}

class FileBrowser extends JFrame implements ActionListener{
    private String obecneKodowanie = "UTF-8";
    JTextField nazwa;
    JTextArea zawartosc;
    JButton wczytaj, zapisz, zmienKodowanie;
    public FileBrowser() { super("File Browser"); }
    
    public void init()
    {
        setSize(500, 450);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        nazwa = new JTextField(20);
        zawartosc = new JTextArea(20,40);
        zapisz = new JButton("Zapisz");
        zapisz.addActionListener(this);
        wczytaj = new JButton("Wczytaj");
        wczytaj.addActionListener(this);
        zmienKodowanie = new JButton("Zmien Kodowanie");
        zmienKodowanie.addActionListener(this);
        
        add(nazwa);
        add(wczytaj);
        add(zapisz);
        add(zmienKodowanie);
        add(new JScrollPane(zawartosc));                       
    }
    
    private void czytaj(File plik)
    {
        try {
            if(!plik.exists()) {
                zawartosc.setText(plik.getName()+" - taki plik nie istnieje");
            } else if (plik.isDirectory()) {
                File[] files = plik.listFiles();
                if(files != null) {
                    zawartosc.setText("Zawartość: \n");
                    for(File f : files) {
                        zawartosc.append(f.getName()+ (f.isDirectory() ? " (Folder)" : "") + "\n");
                    }
                }
            } else
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(plik), obecneKodowanie));
                zawartosc.setText(null);
                String linia;                
                while(true)  {
                    linia = in.readLine();
                    if(linia == null) break;
                    zawartosc.append(linia + "\n");
                }
                in.close();                
            }
        } catch(IOException ex) { System.out.println(ex); }   
        // Zachęcam do pracy z tym przykładem:
        // (1) można użyć klasy FileReader lub Scanner
        // (2) można użyć statycznej metody readAllLines() klasy Files z pakietu java.nio.file
    }
    
    private void zapisz(File plik)
    {
        try
        {
            BufferedWriter out = new BufferedWriter
                                        (new OutputStreamWriter
                                            (new FileOutputStream(plik)));
            out.write(zawartosc.getText());
            out.close();            
        }
        catch (FileNotFoundException e) { System.out.println("Nieprawidłowa nazwa dla pliku"); }  
        catch (IOException e) { System.out.println(e); }  
        // można poeksperymentować z innymi klasami strumieni tekstowych
    }

    private void zmienKodowanie() {
        String kodowanie = JOptionPane.showInputDialog(this, "Podaj kodowanie:", "Zmien kodowanie", JOptionPane.QUESTION_MESSAGE);

        if(kodowanie != null && !kodowanie.trim().isEmpty()) {
            obecneKodowanie = kodowanie;
        } else {
            JOptionPane.showMessageDialog(this, "Nieprawidłowe kodowanie", "Zmien kodowanie", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nazwaPliku = nazwa.getText();
        File plik = new File(nazwaPliku);
        if (e.getSource()==wczytaj)
            czytaj(plik);
        else if (e.getSource()==zapisz)
            zapisz(plik);
        else if (e.getSource()==zmienKodowanie)
            zmienKodowanie();
    }
    
}
