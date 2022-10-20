public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.init();

        Op opCodes = new Op();
        opCodes.main();

        Cpu cpu = new Cpu(memory,opCodes);

        cpu.init();
        int test = 0;
        while (cpu.running && test<50) {
            cpu.main();
            test ++;
        }


        //TEST
        System.out.println(Functions.convertTo24Bit((short) 0, (short) 0, (short) 1));
        System.out.println(Functions.convertTo24Bit((short) 0, (short) 1, (short) 0));
        System.out.println(Functions.convertTo24Bit((short) 1, (short) 0, (short) 0));
        System.out.println(Functions.convertTo24Bit((short) 255, (short) 255, (short) 255));
        short[] arrayTest = Functions.convertFrom24Bit(6000);
        System.out.println(arrayTest[0]+"-"+arrayTest[1]+"-"+arrayTest[2]);

    }
}
