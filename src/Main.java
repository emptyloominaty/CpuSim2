
public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.init();

        Op opCodes = new Op();
        opCodes.main();

        Cpu cpu = new Cpu(memory,opCodes);
        cpu.init();

        Frame frame = new Frame();
        frame.main(cpu,memory);

        int updateFrameA = 0;
        int updateFrameB = 1000000;

        long startTime = System.currentTimeMillis();
        int test = 0;
        while (cpu.running && test<1800000000) {
            cpu.main();

            updateFrameA++;
            if (updateFrameB<updateFrameA) {
                updateFrameA = 0;
                frame.update(cpu); //TODO:16ms
            }

            test ++;
        }
        //System.out.println(memory.load(6002));
        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime+"ms");


        System.out.println("Clock: "+(cpu.cyclesDone/((stopTime - startTime)))/1000+" MHZ");

        System.out.println("Cycles: "+cpu.cyclesDone);
        System.out.println("Instructions: "+cpu.instructionsDone);
        System.out.println("IPC: "+ (double) Math.round(((double) cpu.instructionsDone/cpu.cyclesDone)*100)/100);


        System.out.print("r0: "+cpu.registers[0]);
        System.out.print(" | r1: "+cpu.registers[1]);
        System.out.print(" | r2: "+cpu.registers[2]);
        System.out.print(" | r3: "+cpu.registers[3]);
        System.out.print(" | r4: "+cpu.registers[4]);
        System.out.print(" | r5: "+cpu.registers[5]);
        System.out.print(" | r6: "+cpu.registers[6]);
        System.out.print(" | r7: "+cpu.registers[7]);
        System.out.print(" | r8: "+cpu.registers[8]);
        System.out.print(" | r9: "+cpu.registers[9]);
        System.out.println("");
        System.out.print("r10: "+cpu.registers[10]);
        System.out.print(" | r11: "+cpu.registers[11]);
        System.out.print(" | r12: "+cpu.registers[12]);
        System.out.print(" | r13: "+cpu.registers[13]);
        System.out.print(" | r14: "+cpu.registers[14]);
        System.out.print(" | r15: "+cpu.registers[15]);
        System.out.print(" | r16: "+cpu.registers[16]);
        System.out.print(" | r17: "+cpu.registers[17]);
        System.out.print(" | r18: "+cpu.registers[18]);
        System.out.print(" | r19: "+cpu.registers[19]);
        System.out.println("");
        System.out.print("r20: "+cpu.registers[20]);
        System.out.print(" | r21: "+cpu.registers[21]);
        System.out.print(" | r22: "+cpu.registers[22]);
        System.out.print(" | r23: "+cpu.registers[23]);
        System.out.print(" | r24: "+cpu.registers[24]);
        System.out.print(" | r25: "+cpu.registers[25]);
        System.out.print(" | r26: "+cpu.registers[26]);
        System.out.print(" | r27: "+cpu.registers[27]);
        System.out.print(" | r28: "+cpu.registers[28]);
        System.out.print(" | r29: "+cpu.registers[29]);
        System.out.println("");
        System.out.print("r30: "+cpu.registers[30]);
        System.out.print(" | r31: "+cpu.registers[31]);
        System.out.println("");
        System.out.print("Stack: ");
        //STACK
        int a = 0;
        int l = 0;
        while (a<32) {
            System.out.print(memory.data[a]+" ");
            a++;
            l++;
            if (l>63) {
                l = 0;
                System.out.println();
            }
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
