import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Frame implements ActionListener {
    JButton buttonStartCpu;
    JButton buttonLoad;
    JButton buttonLoadMC;
    JButton buttonLoadUS;
    JTextArea memText;
    Cpu cpu;
    Op opCodes;
    boolean cpuStarted = false;
    boolean firstStart = true;

    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    DecimalFormat df = new DecimalFormat("###,###",dfs);

    int fontSize1 = 22;
    int fontSize2 = 18;
    String fontName = "Consolas";
    JLabel[] labelRegisters = new JLabel[32];
    JLabel[] labelTexts = new JLabel[16];
    JLabel labelSP = new JLabel();
    JLabel labelPC = new JLabel();

    public void main(Cpu cpu,Op opCodes) {
        dfs.setGroupingSeparator(' ');
        this.cpu = cpu;
        this.opCodes = opCodes;

        JPanel panel1 =  new JPanel();
        panel1.setBackground(new Color(60, 60, 60));
        panel1.setBounds(21,50,400,500);
        panel1.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel panel2 =  new JPanel();
        panel2.setBackground(new Color(58, 58, 58));
        panel2.setBounds(425,50,500,500);
        panel2.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel panel3 =  new JPanel();
        panel3.setBackground(new Color(58, 58, 58));
        panel3.setBounds(930,50,310,500);
        panel3.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel panelBottom =  new JPanel();
        panelBottom.setBackground(new Color(60, 60, 60));
        panelBottom.setBounds(20,553,1220,100);
        panelBottom.setBorder(BorderFactory.createLineBorder(Color.black));

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
        frame.add(panel3);
        frame.add(panelBottom);

        //TODO:Memory
        memText = new JTextArea ( 24, 30 );
        memText.setMargin(new Insets(10,10,10,10));
        memText.setFont(new Font(fontName,Font.PLAIN,15));
        memText.setBackground(new Color(21, 21, 21));
        memText.setForeground(new Color(231, 231, 231));
        JScrollPane scroll = new JScrollPane ( memText );
        //scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        /*for (int y = 4096; y<10000; y+=8) {
            memText.append(Integer.toHexString(y)+":");
            for (int x = 0; x<8; x++) {
                memText.append(" "+String.format("%02X", Memory.data[y+x]));
            }
            memText.append("\n");
        }*/

        buttonStartCpu = new JButton();
        buttonStartCpu.addActionListener(this);
        buttonStartCpu.setText("Start");
        buttonStartCpu.setFocusable(false);
        buttonStartCpu.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonStartCpu.setForeground(new Color(220,220,220));
        buttonStartCpu.setBackground(new Color(60,60,60));
        buttonStartCpu.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));

        buttonLoad = new JButton();
        buttonLoad.addActionListener(this);
        buttonLoad.setText("Load");
        buttonLoad.setFocusable(false);
        buttonLoad.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonLoad.setForeground(new Color(220,220,220));
        buttonLoad.setBackground(new Color(60,60,60));
        buttonLoad.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));

        buttonLoadMC = new JButton();
        buttonLoadMC.addActionListener(this);
        buttonLoadMC.setText("Load MC");
        buttonLoadMC.setFocusable(false);
        buttonLoadMC.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonLoadMC.setForeground(new Color(220,220,220));
        buttonLoadMC.setBackground(new Color(60,60,60));
        buttonLoadMC.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));

        buttonLoadUS = new JButton();
        buttonLoadUS.addActionListener(this);
        buttonLoadUS.setText("Load User Storage");
        buttonLoadUS.setFocusable(false);
        buttonLoadUS.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonLoadUS.setForeground(new Color(220,220,220));
        buttonLoadUS.setBackground(new Color(60,60,60));
        buttonLoadUS.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));

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


        panelBottom.add(buttonStartCpu);
        panelBottom.add(buttonLoad);
        panelBottom.add(buttonLoadMC);
        panelBottom.add(buttonLoadUS);

        //Panel2
        panel1.setBorder(new EmptyBorder(10, 5, 10, 5));
        panel2.setBorder(new EmptyBorder(10, 5, 10, 5));
        panel2.setLayout(new GridLayout(18,2));
        panel2.add(labelPC);
        panel2.add(labelSP);
        panel2.add(new JSeparator());
        panel2.add(new JSeparator());
        for (int i = 0; i<32; i++) {
            panel2.add(labelRegisters[i]);
        }
        panel3.add(scroll);
    }

    public void update(Cpu cpu) {
        for (int i = 0; i<labelRegisters.length; i++) {
            labelRegisters[i].setText(" r"+i+": "+cpu.registers[i]+" ");
        }
        labelPC.setText(" PC: "+cpu.programCounter+" ");
        labelSP.setText(" SP: "+cpu.stackPointer+" ");
        labelTexts[1].setText("Clock: "+getClock(cpu.clock));
        labelTexts[2].setText("Cycles Done: "+df.format(cpu.cyclesDone));
        labelTexts[3].setText("Instructions Done: "+df.format(cpu.instructionsDone));
        labelTexts[4].setText("IPC: "+ (double) Math.round(((double) cpu.instructionsDone/cpu.cyclesDone)*100)/100);
    }

    public String getClock(long clock) {
        if (clock>1000000) {
            return ((double) Math.round((clock/1000000d)*10)/10) +"MHz";
        } else if (clock>1000) {
            return ((double) Math.round((clock/1000d)*10)/10) +"kHz";
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
        } else if (e.getSource()==buttonLoad) {
            //Assemble
            Assembler.assemble(memText.getText(), opCodes);
        } else if (e.getSource()==buttonLoadMC) {
            //Load Machine Code
            Assembler.loadMachineCode(memText.getText());
        } else if (e.getSource()==buttonLoadUS) {
            //Load UserStorage
        }
    }
}
