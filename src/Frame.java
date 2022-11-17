import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Frame implements ActionListener, KeyListener {
    JButton buttonStartCpu;
    JButton buttonLoad;
    JButton buttonLoadMC;
    JButton buttonLoadUS;
    JButton buttonMemory;
    JButton buttonScreen;
    JButton buttonTest;
    JButton buttonTest2;
    JButton buttonTest3;
    JButton buttonCpuDebug;
    JTextField inputClock;
    JTextArea memText;
    Cpu cpu;
    Op opCodes;
    Screen screen;
    Timers timers;
    ScreenFrame screenFrame;
    Device[] devices;
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

    public void main(Cpu cpu,Op opCodes, Screen screen, ScreenFrame screenFrame,Device[] devices) {
        dfs.setGroupingSeparator(' ');
        this.cpu = cpu;
        this.opCodes = opCodes;
        this.screen = screen;
        this.screenFrame = screenFrame;
        this.timers = new Timers();
        this.devices = devices;

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

        panel1.addKeyListener(this);
        panel1.setFocusable( true );
        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel1.requestFocusInWindow();
                System.out.println("Focus");
            }
        });

        memText = new JTextArea ( 24, 30 );
        memText.setMargin(new Insets(10,10,10,10));
        memText.setFont(new Font(fontName,Font.PLAIN,15));
        memText.setBackground(new Color(21, 21, 21));
        memText.setForeground(new Color(231, 231, 231));
        JScrollPane scroll = new JScrollPane ( memText );
        memText.setText(";CODE EDITOR;");
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

        buttonMemory = new JButton();
        buttonMemory.addActionListener(this);
        buttonMemory.setText("Show Memory");
        buttonMemory.setFocusable(false);
        buttonMemory.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonMemory.setForeground(new Color(220,220,220));
        buttonMemory.setBackground(new Color(60,60,60));
        buttonMemory.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));

        buttonScreen = new JButton();
        buttonScreen.addActionListener(this);
        buttonScreen.setText("Show Screen");
        buttonScreen.setFocusable(false);
        buttonScreen.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonScreen.setForeground(new Color(220,220,220));
        buttonScreen.setBackground(new Color(60,60,60));
        buttonScreen.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));


        buttonCpuDebug = new JButton();
        buttonCpuDebug.addActionListener(this);
        buttonCpuDebug.setText("Debug: False");
        buttonCpuDebug.setFocusable(false);
        buttonCpuDebug.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonCpuDebug.setForeground(new Color(220,220,220));
        buttonCpuDebug.setBackground(new Color(60,60,60));
        buttonCpuDebug.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));

        buttonTest = new JButton();
        buttonTest.addActionListener(this);
        buttonTest.setText("INT 20");
        buttonTest.setFocusable(false);
        buttonTest.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonTest.setForeground(new Color(220,220,220));
        buttonTest.setBackground(new Color(60,60,60));
        buttonTest.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));

        buttonTest2 = new JButton();
        buttonTest2.addActionListener(this);
        buttonTest2.setText("INT 21");
        buttonTest2.setFocusable(false);
        buttonTest2.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonTest2.setForeground(new Color(220,220,220));
        buttonTest2.setBackground(new Color(60,60,60));
        buttonTest2.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));


        buttonTest3 = new JButton();
        buttonTest3.addActionListener(this);
        buttonTest3.setText("TEST");
        buttonTest3.setFocusable(false);
        buttonTest3.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        buttonTest3.setForeground(new Color(220,220,220));
        buttonTest3.setBackground(new Color(60,60,60));
        buttonTest3.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.5f)));



        inputClock = new JTextField();
        inputClock.setPreferredSize(new Dimension(200,35));
        inputClock.setFont(new Font(fontName,Font.PLAIN,fontSize1));
        inputClock.setForeground(new Color(220,220,220));
        inputClock.setBackground(new Color(60,60,60));
        inputClock.setCaretColor(new Color(220,220,220));
        inputClock.setText("1000000"); //MAX TODO: TODO: TODO

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

        labelTexts[11].setText(cpu.op+"");

        panelBottom.add(inputClock);
        panelBottom.add(buttonStartCpu);
        panelBottom.add(buttonLoad);
        panelBottom.add(buttonLoadMC);
        panelBottom.add(buttonLoadUS);
        panelBottom.add(buttonMemory);
        panelBottom.add(buttonScreen);
        panelBottom.add(buttonTest);
        panelBottom.add(buttonTest2);
        panelBottom.add(buttonTest3);
        panelBottom.add(buttonCpuDebug);

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

        double cpu1p = cpu.cyclesTotal/100d;
        if (cpu1p==0) {
            cpu1p = 1;
        }

        double cpuUsage = Math.round(cpu.cyclesExecuting/cpu1p*10d)/10d;
        if (cpuUsage>100) {
            cpuUsage = 100;
        }
        labelTexts[5].setText("Cpu Usage: "+cpuUsage+"%");


        if (!cpu.interruptHw) {
            labelTexts[9].setText("");
        } else {
            labelTexts[9].setText("INT");
        }
        labelTexts[10].setText(opCodes.names2[cpu.op]);
        labelTexts[11].setText(String.format("%02X",cpu.op)+" "+String.format("%02X",cpu.instructionData[1])+" "+String.format("%02X",cpu.instructionData[2])+" "+String.format("%02X",cpu.instructionData[3])+" "+String.format("%02X",cpu.instructionData[4])+" "+String.format("%02X",cpu.instructionData[5]));

        //labelTexts[14].setText("Devices Clock: 1:"+cpu.devicesBusRatio+" ("+getClock(cpu.clock/cpu.devicesBusRatio)+")");
        //labelTexts[15].setText("Memory Clock: 1:1 ("+getClock(cpu.clock)+")");

        labelTexts[14].setText("Devices Clock: "+getClock(cpu.clock/cpu.devicesBusRatio)+" (1:"+cpu.devicesBusRatio+")");
        labelTexts[15].setText("Memory Clock: "+getClock(cpu.clock)+" (1:1)");

        screen.updateS();
        //Timers
        int timer1Val = Functions.convertTo16Bit(Memory.load(0xFF0010),Memory.load(0xFF0011));
        if (timer1Val!=0) {
            timers.setTimer1(timer1Val,cpu);
        } else {
            timers.cancelTimer1();
        }
        int timer2Val = Functions.convertTo16Bit(Memory.load(0xFF0012),Memory.load(0xFF0013));
        if (timer2Val!=0) {
            timers.setTimer2(timer2Val,cpu);
        } else {
            timers.cancelTimer2();
        }
        int timer3Val = Functions.convertTo16Bit(Memory.load(0xFF0014),Memory.load(0xFF0015));
        if (timer3Val!=0) {
            timers.setTimer3(timer3Val,cpu);
        } else {
            timers.cancelTimer3();
        }
        int timer4Val = Functions.convertTo16Bit(Memory.load(0xFF0016),Memory.load(0xFF0017));
        if (timer4Val!=0) {
            timers.setTimer4(timer4Val,cpu);
        } else {
            timers.cancelTimer4();
        }
    }

    public void updateDevices() {
        for (int i = 0; i<devices.length; i++) {
            devices[i].interrupt();
        }
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
            if (inputClock.getText()=="MAX" || !Functions.isNumeric(inputClock.getText())) {
                cpu.maxClock = true;
            } else {
                cpu.maxClock = false;
                cpu.clockSet = Long.parseLong(inputClock.getText());
            }
            if (cpuStarted) {
                buttonStartCpu.setText("Start");
                System.out.println("STOPPED");
                cpuStarted = false;
                cpu.running = false;
                System.out.println(cpu.getName() + " (" + cpu.getState() + ")");
            } else {
                buttonStartCpu.setText("Stop");
                cpuStarted = true;
                if (firstStart) {
                    cpu.start();
                    firstStart = false;
                } else {
                    cpu.resetCpu();
                }
                cpu.running = true;
                System.out.println("STARTED");
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
        } else if (e.getSource()==buttonMemory) {
            MemoryFrame.main();
        } else if (e.getSource()==buttonScreen) {
            screenFrame.main();
        } else if (e.getSource()==buttonTest) {
            cpu.interrupt((byte) 20);
        } else if (e.getSource()==buttonTest2) {
            cpu.interrupt((byte) 21);
        } else if (e.getSource()==buttonTest3) {
            //cpu.devicesBusRatio++;
            int x = 0;
            for (int i = 0x3000; i<0x3200 ;i++) {
                System.out.print(Memory.load(i)+" ");
                x++;
                if (x>=64) {
                    x = 0;
                    System.out.println("");
                }
            }

        } else if (e.getSource()==buttonCpuDebug) {
            if (cpu.debug) {
                buttonCpuDebug.setText("Debug: False");
                cpu.debug = false;
            } else {
                buttonCpuDebug.setText("Debug: True");
                cpu.debug = true;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Memory.store(0xFF0000, (short) e.getKeyCode());
        cpu.interrupt((byte) 1);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
