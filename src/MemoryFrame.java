import javax.swing.*;
import java.awt.*;

public class MemoryFrame  {
    static JTextArea memText;
    static String fontName = "Consolas";

    public static void main() {

        memText = new JTextArea ( 25, 75 );
        memText.setMargin(new Insets(10,10,10,10));
        memText.setFont(new Font(fontName,Font.PLAIN,15));
        memText.setBackground(new Color(21, 21, 21));
        memText.setForeground(new Color(231, 231, 231));
        JScrollPane scroll = new JScrollPane ( memText );

        JFrame frame = new JFrame();
        frame.setTitle("CPUSim 2 - Memory");
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(40,40,40));
        frame.setSize(800,600);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setFocusable(true);

        JPanel panel1 =  new JPanel();
        panel1.setBackground(new Color(60, 60, 60));
        panel1.setBounds(20,20,745,530);
        panel1.setBorder(BorderFactory.createLineBorder(Color.black));

        frame.add(panel1);
        panel1.add(scroll);

        for (int y = 0; y<65536; y+=16) {
            memText.append(Integer.toHexString(y)+":");
            for (int x = 0; x<16; x++) {
                memText.append(" "+String.format("%02X", Memory.data[y+x]));
            }
            memText.append("\n");
        }

        //TEST

        /*for (int y = 16715760; y<16716000 ; y+=16) {
            memText.append(Integer.toHexString(y)+":");
            for (int x = 0; x<16; x++) {
                memText.append(" "+String.format("%02X", Memory.data[y+x]));
            }
            memText.append("\n");
        }*/


    }

}
