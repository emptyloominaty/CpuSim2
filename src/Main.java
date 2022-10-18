public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.init();

        Op opCodes = new Op();
        opCodes.main();

        Cpu cpu = new Cpu(memory,opCodes);

        cpu.init();
        int test = 0;
        while (cpu.running && test<20) {
            System.out.println(test+".");
            cpu.main();
            test ++;
        }


    }
}
