import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Screen extends JPanel implements ActionListener {
    String fontName = "Consolas";
    short width = 640;
    short height = 480;
    byte color = 1;
    int frameBufferStart = 0xA0000A;
    boolean test = false;
    Timer timer;

    Screen() {
        timer = new Timer(1000/20, this);
        timer.start();
    }
    
    public void paint(Graphics g) {
        //Graphics2D graphic2d = (Graphics2D) g;
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 1280, 720);

        /*if (test) {
            g.setColor(new Color(255,255,255));
            g.fillRect(680, 10, 10, 10);
            test = false;
        } else {
            g.setColor(new Color(255, 16, 16));
            g.fillRect(680, 10, 10, 10);
            test = true;
        }*/

        int addr = frameBufferStart;
        for (int y = 0; y<height; y++) {
            for (int x = 0; x<width; x++) {
                //TODO:COLORS
                if (Memory.load(addr)==1) {
                    g.setColor(new Color(255,255,255));
                } else {
                    g.setColor(new Color(0, 0, 0));
                }
                g.fillRect(x, y, 1, 1);
                addr++;
            }
        }
    }

    public void updateS() {
        width = (short) Functions.convertTo16Bit(Memory.load(0xA00001),Memory.load(0xA00002));
        height = (short) Functions.convertTo16Bit(Memory.load(0xA00003),Memory.load(0xA00004));
        color = (byte) Memory.load(0xA00000);
        frameBufferStart = Functions.convertTo24Bit(Memory.load(0xA00005),Memory.load(0xA00006),Memory.load(0xA00007));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
