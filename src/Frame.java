import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame implements ActionListener {
    int fontSize1 = 22;
    int fontSize2 = 18;
    String fontName = "Consolas";
    JLabel[] labelRegisters = new JLabel[32];
    JLabel labelSP = new JLabel();
    JLabel labelPC = new JLabel();

    public void main(Cpu cpu, Memory memory) {

        JPanel panel1 =  new JPanel();
        panel1.setBackground(new Color(115, 115, 115));
        panel1.setBounds(1,50,300,500);
        panel1.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel panel2 =  new JPanel();
        panel2.setBackground(new Color(107, 107, 107));
        panel2.setBounds(302,50,300,500);
        panel2.setBorder(BorderFactory.createLineBorder(Color.black));

        for (int i = 0; i<labelRegisters.length; i++) {
            labelRegisters[i] = new JLabel();
            labelRegisters[i].setText("| r"+i+": "+cpu.registers[i]+" ");
            labelRegisters[i].setHorizontalAlignment(JLabel.CENTER);
            labelRegisters[i].setVerticalAlignment(JLabel.TOP);
            labelRegisters[i].setForeground(new Color(220,220,220));
            labelRegisters[i].setFont(new Font(fontName,Font.PLAIN,fontSize2));
        }
        labelPC.setText("| PC: "+cpu.programCounter+" ");
        labelPC.setHorizontalAlignment(JLabel.CENTER);
        labelPC.setVerticalAlignment(JLabel.TOP);
        labelPC.setForeground(new Color(220,220,220));
        labelPC.setFont(new Font(fontName,Font.PLAIN,fontSize2));

        labelSP.setText("| PC: "+cpu.stackPointer+" ");
        labelSP.setHorizontalAlignment(JLabel.CENTER);
        labelSP.setVerticalAlignment(JLabel.TOP);
        labelSP.setForeground(new Color(220,220,220));
        labelSP.setFont(new Font(fontName,Font.PLAIN,fontSize2));

        JFrame frame = new JFrame();
        frame.setTitle("CPUSim 2");
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(40,40,40));  //30,40,60?
        frame.setSize(1280,720);
        frame.setVisible(true);
        frame.setLayout(null);
        //frame.setLayout(new FlowLayout());
        frame.setFocusable(true);
        frame.add(panel1);
        frame.add(panel2);
        panel2.add(labelSP);
        panel2.add(labelPC);
        for (int i = 0; i<32; i++) {
            panel2.add(labelRegisters[i]);
        }


    }

    public void update(Cpu cpu) {
        for (int i = 0; i<labelRegisters.length; i++) {
            labelRegisters[i].setText("| r"+i+": "+cpu.registers[i]+" ");
        }
        labelPC.setText("| PC: "+cpu.programCounter+" ");
        labelSP.setText("| SP: "+cpu.stackPointer+" ");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
