import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

class Notepad extends JFrame
{
  private String filename;
  private JMenuItem open = new JMenuItem("Open");
  private JMenuItem save = new JMenuItem("Save");
  private JTextArea text = new JTextArea();
  private JScrollPane pScroll = new JScrollPane(
      text, 
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
  
// -------------------------------------------
  // klasa obslugi przycisku open
  class OpenFile implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      JFileChooser fc = new JFileChooser(".");
      int val = fc.showOpenDialog(null);
      if(val == JFileChooser.APPROVE_OPTION) 
      {
        filename = fc.getSelectedFile().getPath();
        readFile();
      }
    }
  }

// -------------------------------------------
  // klasa obslugi przycisku save
  class SaveFile implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      saveFile();
    }
  }

// -------------------------------------------

  private void readFile()
  {
    try {
      BufferedReader odczyt = new BufferedReader(new FileReader(filename));
      StringBuilder plik = new StringBuilder();
      String line;

      while( (line = odczyt.readLine()) != null ) {
        plik.append(line).append("\n");
      }
      text.setText(plik.toString());

      odczyt.close();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Błąd odczytu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void saveFile()
  {
    try {
      BufferedWriter zapis = new BufferedWriter(new FileWriter(filename));

      String tekst = text.getText();
      zapis.write(tekst);

      zapis.close();
      JOptionPane.showMessageDialog(this, "Dane zapisane.", "Zapisano", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Błąd zapisu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
    }
  }
  
// -------------------------------------------

  // konstruktor okna
  public Notepad()
  {
    super("Prosty notatnik");

    // tworzymy menu okna
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    menu.add(open);
    menu.add(save);
    menuBar.add(menu);
    this.setJMenuBar(menuBar);
    
    this.getContentPane().add(pScroll);
    text.setLineWrap(true);

    open.addActionListener(new OpenFile());
    save.addActionListener(new SaveFile());

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(500,500);
    setVisible(true);
  }
  
// -------------------------------------------

  public static void main(String[] args)
  {
    new Notepad();
  }
}
