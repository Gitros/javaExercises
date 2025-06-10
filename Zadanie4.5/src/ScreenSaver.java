// rozwiązanie zadania IV.9 jest kodem bazowym, 
// należy uzupełnić i zmodyfikować zgodnie z dołączonym diagramem i treścia zadania
import java.awt.*;
import javax.swing.*;
import java.util.*;

class FlyingText extends Thread {
    private JPanel window;
    private int x, y;
    private Color color;
    private String text;
    private Font font;
    private int width, height;
    
    FlyingText(JPanel window, String text) {
        this.window = window;
        font = new Font("Tahoma",Font.PLAIN,40);
        color = Color.darkGray;
        this.text = text;        
    }
    
    FlyingText(JPanel window, String text, Font f, Color c) {
        this(window, text);        
        font = f;
        color = c;
    }

    public void draw(Graphics g) {
        synchronized(window) {
            Font tempFont = g.getFont();
            Color tempColor = g.getColor();

            g.setFont(font);
            g.setColor(color);
            g.drawString(text, x, y);

            g.setFont(tempFont);
            g.setColor(tempColor);
        }
    }
    public void setRandomLocation() {
        Random r = new Random(); 
        Graphics g = window.getGraphics();                               
        width = g.getFontMetrics(font).stringWidth(text);
        height = g.getFontMetrics(font).getHeight();//-15;
        
        y = height  + r.nextInt(window.getHeight() - height + 1);
        x = r.nextInt(window.getWidth() - width + 1);
    }
    
    public void run() {
        //Random r = new Random();
        while(true) {                                    
            setRandomLocation();
            window.repaint();
            try { Thread.sleep(500); } catch(Exception e) {};                        
        }
    }
}

class MovingCircle extends Thread {
    private JPanel window;
    private int x, y, diameter;
    private int dx = 5, dy = 5; // kierunki ruchu
    private Color color;

    MovingCircle(JPanel window, int diameter, Color color) {
        this.window = window;
        this.diameter = diameter;
        this.color = color;

        Random r = new Random();
        x = r.nextInt(window.getWidth() - diameter);
        y = r.nextInt(window.getHeight() - diameter);
    }

    public void draw(Graphics g) {
        synchronized(window) {
            Color temp = g.getColor();
            g.setColor(color);
            g.fillOval(x, y, diameter, diameter);
            g.setColor(temp);
        }
    }

    public void run() {
        while(true) {
            move();
            window.repaint();
            try { Thread.sleep(50); } catch(Exception e) {}
        }
    }

    private void move() {
        if(x + dx < 0 || x + diameter + dx > window.getWidth()) dx = -dx;
        if(y + dy < 0 || y + diameter + dy > window.getHeight()) dy = -dy;

        x += dx;
        y += dy;
    }
}

public class ScreenSaver extends JPanel {
    private FlyingText[] texts;
    private MovingCircle[] circle;

    ScreenSaver() {
        super();
        setBackground(Color.white);
        setDoubleBuffered(true);
    }

    public void startAnimation() {
        texts = new FlyingText[] {
                new FlyingText(this, "Java", new Font("SansSerif", Font.BOLD, 32), Color.RED),
                new FlyingText(this, "is", new Font("SansSerif", Font.BOLD, 32), Color.RED),
                new FlyingText(this, "cool", new Font("SansSerif", Font.BOLD, 32), Color.RED)
        };

        circle = new MovingCircle[] {
                new MovingCircle(this, 40, Color.RED)
        };

        for(FlyingText t : texts) t.start();
        for(MovingCircle s : circle) s.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(texts != null) {
            for(FlyingText t : texts) t.draw(g);
        }
        if(circle != null) {
            for(MovingCircle s : circle) s.draw(g);
        }
    }
}


class ScreenSaverWindow extends JFrame {

    private ScreenSaver flyingSpace;

    ScreenSaverWindow() {
        super("Screen saver - flying texts");        
        init();
        setVisible(true);
        flyingSpace.startAnimation();
    }

    private void init() {
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        flyingSpace = new ScreenSaver();        
        add(flyingSpace);
    }

    public static void main(String[] a) {
        new ScreenSaverWindow();
    }
}
