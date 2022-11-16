
public class Main {
    public static void main(String[] args) {
        Memory.init();
        Memory.loadTestProgram();

        Op opCodes = new Op();
        opCodes.main();

        Cpu cpu = new Cpu(opCodes);
        cpu.init();

        Screen screen = new Screen();
        ScreenFrame screenFrame = new ScreenFrame();

        Device[] devices = new Device[8];

        devices[0] = new Device((byte) 0,1,"test",cpu, (short) 255, (short) 24,65536);

        devices[1] = new Device((byte) 1,0,"",cpu, (short) 0, (short) 24,256);
        devices[2] = new Device((byte) 2,0,"",cpu, (short) 0, (short) 24,256);
        devices[3] = new Device((byte) 3,0,"",cpu, (short) 0, (short) 24,256);
        devices[4] = new Device((byte) 4,0,"",cpu, (short) 0, (short) 24,256);
        devices[5] = new Device((byte) 5,0,"",cpu, (short) 0, (short) 24,256);
        devices[6] = new Device((byte) 6,0,"",cpu, (short) 0, (short) 24,256);
        devices[7] = new Device((byte) 7,0,"",cpu, (short) 0, (short) 24,256);

        Frame frame = new Frame();
        frame.main(cpu,opCodes,screen,screenFrame,devices);

        cpu.sendFrame(frame);

    }
}
