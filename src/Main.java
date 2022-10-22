
public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.init();

        Op opCodes = new Op();
        opCodes.main();

        Cpu cpu = new Cpu(memory,opCodes);
        cpu.init();

        long startTime = System.currentTimeMillis();
        int test = 0;
        while (cpu.running && test<1800000000) {
            cpu.main();
            test ++;
        }
        //System.out.println(memory.load(6002));
        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime+"ms");



        System.out.println("Cycles: "+cpu.cyclesDone);
        System.out.println("Instructions: "+cpu.instructionsDone);
        System.out.println("IPC: "+ (double) Math.round(((double) cpu.instructionsDone/cpu.cyclesDone)*100)/100);
        System.out.print("r20: "+cpu.registers[20]);
        System.out.print("| r21: "+cpu.registers[21]);
        System.out.print("| r22: "+cpu.registers[22]);
        System.out.print("| r23: "+cpu.registers[23]);
        System.out.print("| r24: "+cpu.registers[24]);
        System.out.print("| r25: "+cpu.registers[25]);

        System.out.println("");
        System.out.print("Stack: ");
        //STACK
        int a = 0;
        while (a<100) {
            System.out.print(memory.data[a]+" ");
            a++;
        }

        //TEST
        /*System.out.println(Functions.convertTo24Bit((short) 0, (short) 0, (short) 1));
        System.out.println(Functions.convertTo24Bit((short) 0, (short) 1, (short) 0));
        System.out.println(Functions.convertTo24Bit((short) 1, (short) 0, (short) 0));
        System.out.println(Functions.convertTo24Bit((short) 255, (short) 255, (short) 255));*/
        //short[] arrayTest = Functions.convertFrom24Bit(5013);
        //System.out.println(arrayTest[0]+"-"+arrayTest[1]+"-"+arrayTest[2]);

    }
}
