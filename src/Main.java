
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

        Frame frame = new Frame();
        frame.main(cpu,opCodes,screen,screenFrame);

        cpu.sendFrame(frame);



    }
}
