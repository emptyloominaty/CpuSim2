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

    byte scaling = 2;

    short prevColor = 0;
    int rr = 0;
    int gg = 0;
    int bb = 0;

    Screen() {
        timer = new Timer(1000/20, this);
        timer.start();
    }

    public void paint(Graphics g) {
        //Graphics2D graphic2d = (Graphics2D) g;
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 1600, 1200);

        /*if (test) {
            g.setColor(new Color(255,255,255));
            g.fillRect(680, 10, 10, 10);
            test = false;
        } else {
            g.setColor(new Color(255, 16, 16));
            g.fillRect(680, 10, 10, 10);
            test = true;
        }*/

        short height2 = (short) (height*scaling);
        short width2 = (short) (width*scaling);

        int addr = frameBufferStart;
        for (int y = 0; y<height2; y+=scaling) {
            for (int x = 0; x<width2; x+=scaling) {
                short colorVal = Memory.load(addr);
                if (prevColor!=colorVal) {
                    prevColor = colorVal;
                    int red = ((colorVal & 0xE0) >> 5);
                    int green = ((colorVal & 0x1C) >> 2);
                    int blue = (colorVal & 0x03);
                    rr = red*36;
                    gg = green*36;
                    bb = blue*85;
                }
                g.setColor(new Color(rr,gg,bb));
                g.fillRect(x, y, scaling, scaling);
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
