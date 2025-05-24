import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Komunikator extends JFrame implements ActionListener, KeyListener {

    JTextArea obszar;
    JPanel dolny;
    JTextField wpis;
    JButton wyslij;

    Komunikator() {
        super("Mój pierwszy nibyKomunikator, wersja 1");
    }

    public void init() {
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        obszar = new JTextArea();
        obszar.setEditable(false);
        JScrollPane js = new JScrollPane(obszar);
        add(js, BorderLayout.CENTER);

        dolny = new JPanel();
        dolny.setLayout(new FlowLayout());
        wpis = new JTextField(40);
        wpis.addKeyListener(this);        
        dolny.add(wpis);
        wyslij = new JButton("Wyślij");
        wyslij.addActionListener(this);
        dolny.add(wyslij);
        add(dolny, BorderLayout.SOUTH);

     // dla estetyki, aby obszar tekstowy
     // nie wypełniał okna "po brzegi"
        add(new JPanel(), BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);

        setVisible(true);
     // ustawiam kursor (fokus) na polu 'wpis'
        wpis.requestFocus();
    }

    private void sendMessage() {
        if(!wpis.getText().isBlank()) {
            obszar.append(wpis.getText() + "\n");
            wpis.setText("");
            wpis.requestFocus();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Object zrodlo = e.getSource();
        if(zrodlo == wyslij) { sendMessage(); }        
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) { sendMessage(); }            
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String a[]) {
        Komunikator k = new Komunikator();
        k.init();
    }
}
