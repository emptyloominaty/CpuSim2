public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.init();

        Cpu cpu = new Cpu(memory);

        cpu.init();
        int test = 0;
        while (cpu.running && test<10) {
            cpu.main();
            test ++;
        }


    }
}
