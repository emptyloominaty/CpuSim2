import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame implements ActionListener {
    JButton buttonStartCpu;
    Cpu cpu;
    Op opCodes;
    boolean cpuStarted = false;
    boolean firstStart = true;

    int fontSize1 = 22;
    int fontSize2 = 18;
    String fontName = "Consolas";
    JLabel[] labelRegisters = new JLabel[32];
    JLabel[] labelTexts = new JLabel[16];
    JLabel labelSP = new JLabel();
    JLabel labelPC = new JLabel();

    public void main(Cpu cpu,Op opCodes) {
        this.cpu = cpu;
        this.opCodes = opCodes;

        JPanel panel1 =  new JPanel();
        panel1.setBackground(new Color(60, 60, 60));
        panel1.setBounds(1,50,400,500);
        panel1.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel panel2 =  new JPanel();
        panel2.setBackground(new Color(58, 58, 58));
        panel2.setBounds(402,50,500,500);
        panel2.setBorder(BorderFactory.createLineBorder(Color.black));

        for (int i = 0; i<labelRegisters.length; i++) {
            labelRegisters[i] = new JLabel();
            labelRegisters[i].setText(" r"+i+": "+cpu.registers[i]+" ");
            labelRegisters[i].setHorizontalAlignment(JLabel.CENTER);
            labelRegisters[i].setVerticalAlignment(JLabel.TOP);
            labelRegisters[i].setForeground(new Color(220,220,220));
            labelRegisters[i].setFont(new Font(fontName,Font.PLAIN,fontSize2));
        }
        labelPC.setText(" PC: "+cpu.programCounter+" ");
        labelPC.setHorizontalAlignment(JLabel.CENTER);
        labelPC.setVerticalAlignment(JLabel.TOP);
        labelPC.setForeground(new Color(220,220,220));
        labelPC.setFont(new Font(fontName,Font.PLAIN,fontSize2));

        labelSP.setText(" PC: "+cpu.stackPointer+" ");
        labelSP.setHorizontalAlignment(JLabel.CENTER);
        labelSP.setVerticalAlignment(JLabel.TOP);
        labelSP.setForeground(new Color(220,220,220));
        labelSP.setFont(new Font(fontName,Font.PLAIN,fontSize2));


        for (int i = 0; i<labelTexts.length; i++) {
            labelTexts[i] = new JLabel();
            labelTexts[i].setText("  ");
            labelTexts[i].setHorizontalAlignment(JLabel.CENTER);
            labelTexts[i].setVerticalAlignment(JLabel.TOP);
            labelTexts[i].setForeground(new Color(220,220,220));
            labelTexts[i].setFont(new Font(fontName,Font.PLAIN,fontSize2));
        }

        JFrame frame = new JFrame();
        frame.setTitle("CPUSim 2");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(40,40,40));  //30,40,60?
        frame.setSize(1280,720);
        frame.setVisible(true);
        frame.setLayout(null);
        //frame.setLayout(new FlowLayout());
        frame.setFocusable(true);
        frame.add(panel1);
        frame.add(panel2);

        buttonStartCpu = new JButton();
        buttonStartCpu.setBounds(30,20,100,24);
        buttonStartCpu.addActionListener(this);
        buttonStartCpu.setText("Start");
        buttonStartCpu.setFocusable(false);
        buttonStartCpu.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonStartCpu.setForeground(new Color(220,220,220));
        buttonStartCpu.setBackground(new Color(60,60,60));
        buttonStartCpu.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));
        frame.add(buttonStartCpu);

        //Panel1
        panel1.setLayout(new GridLayout(16,1));
        for (int i = 0; i<labelTexts.length; i++) {
            panel1.add(labelTexts[i]);
        }
        labelTexts[0].setText("EMP 2 24bit CPU");
        labelTexts[1].setText("Clock: "+cpu.clock/1000000+"MHz");
        labelTexts[2].setText("Cycles Done: "+cpu.cyclesDone);
        labelTexts[3].setText("Instructions Done: "+cpu.instructionsDone);
        labelTexts[4].setText("IPC: "+ (double) Math.round(((double) cpu.instructionsDone/cpu.cyclesDone)*100)/100);

        //Panel2
        panel2.setLayout(new GridLayout(18,2));
        panel2.add(labelPC);
        panel2.add(labelSP);
        panel2.add(new JSeparator());
        panel2.add(new JSeparator());
        for (int i = 0; i<32; i++) {
            panel2.add(labelRegisters[i]);
        }


    }

    public void update(Cpu cpu) {
        for (int i = 0; i<labelRegisters.length; i++) {
            labelRegisters[i].setText(" r"+i+": "+cpu.registers[i]+" ");
        }
        labelPC.setText(" PC: "+cpu.programCounter+" ");
        labelSP.setText(" SP: "+cpu.stackPointer+" ");
        labelTexts[1].setText("Clock: "+getClock(cpu.clock));
        labelTexts[2].setText("Cycles Done: "+cpu.cyclesDone);
        labelTexts[3].setText("Instructions Done: "+cpu.instructionsDone);
        labelTexts[4].setText("IPC: "+ (double) Math.round(((double) cpu.instructionsDone/cpu.cyclesDone)*100)/100);
    }

    public String getClock(long clock) {
        if (clock>1000000) {
            return clock/1000000+"MHz";
        } else if (clock>1000) {
            return clock/1000+"kHz";
        } else {
            return clock+"Hz";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==buttonStartCpu) {
            if (cpuStarted) {
                buttonStartCpu.setText("Start");
                System.out.println("STOPPED");
                cpuStarted = false;
                cpu.running = false;
                System.out.println(cpu.getName() + " (" + cpu.getState() + ")");
            } else {
                buttonStartCpu.setText("Stop");
                cpuStarted = true;
                System.out.println("STARTED");
                cpu.running = true;
                if (firstStart) {
                    cpu.start();
                    firstStart = false;
                } else {
                    cpu.resetCpu();
                }
                System.out.println(cpu.getName() + " (" + cpu.getState() + ")");
            }

        }
    }
}
