import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Screen extends JPanel{
    static String fontName = "Consolas";

    public static void main() {
        JFrame frame = new JFrame();
        frame.setTitle("CPUSim 2 - Screen");
        frame.setResizable(false);
        //frame.getContentPane().setBackground(new Color(10,10,10));
        frame.setSize(1000,700);
        frame.setVisible(true);
        frame.add(new Screen());
    }

    public void paint(Graphics g) {
        Graphics2D graphic2d = (Graphics2D) g;
        graphic2d.setColor(Color.GRAY);
        graphic2d.fillRect(0, 0, 1000, 700);
    }

    

    public static void update() {

    }

}
