public class Cpu {

    Memory memory;
    Op opCodes;

    public Cpu(Memory memory, Op opCodes) {
        this.memory = memory;
        this.opCodes = opCodes;
    }

    int[] registers = new int[32];
    int[] interruptPointers = new int[32];
    int programCounter = 4096;
    short stackPointer = 0;

    //flags
    boolean flagCarry = false;
    boolean flagZero = false;
    boolean flagSign = false;
    boolean flagOverflow = false;
    boolean flagInterruptDisable = false;

    public void init() {
        registers = new int[32];
        interruptPointers = new int[32];
        programCounter = 4096;
        stackPointer = 0;

        //flags
        flagCarry = false;
        flagZero = false;
        flagSign = false;
        flagOverflow = false;
        flagInterruptDisable = false;
    }

    boolean running = true;
    int programCounterMax = 4259839;
    short stackPointerMax = 4095;
    byte cpuPhase = 0;
    byte op = 0;
    byte bytes = 0;
    byte cycles = 0;
    byte cyclesI = 0;
    byte bytesLeft = 0;
    byte memThisCycleLeft = 3;
    short[] instructionCache = new short[6];
    byte instructionCacheIdx = 0;
    byte ramBus = 3; //bytes per clock

    public void main() {
        memThisCycleLeft = 3;
        switch(cpuPhase) {
            case 0:
                //fetch opcode
                fetchOpCode();
                break;

            case 1:
                //fetch other bytes
                fetchOtherBytes();
                break;
            case 2:
                //execute
                if (cyclesI>0) {
                    cyclesI--;
                }
                if (cyclesI<=0) {
                    cpuPhase++;
                }
                break;
            case 3:
                //execute end
                execute();
                break;

        }

    }

    public void fetchOpCode() {
        short val = loadByte();
        op = (byte) val;
        bytes = opCodes.codes[val][0];
        bytesLeft = (byte) (bytes-1);
        cycles = opCodes.codes[val][1];
        cyclesI = (byte) (cycles-1);
        instructionCacheIdx = 1;
        memThisCycleLeft = 2;
        instructionCache[0] = val;

        System.out.print(val);

        programCounter ++;
        cpuPhase++;
        if (op==0) {
            stopCpu();
        }
    }

    public void fetchOtherBytes() {
        while (memThisCycleLeft>0 && bytesLeft>0) {
            short val = loadByte();
            memThisCycleLeft --;
            System.out.print(" "+val);
            instructionCache[instructionCacheIdx] = val;
            instructionCacheIdx++;
            programCounter ++;
            bytesLeft--;
        }
        if (bytesLeft<=0) {
            System.out.println("");
            cpuPhase++;
        }
    }

    public void execute() {
        //TODO:
        switch(op) {
            case 1: //ADD  r1+r2=r3
                registers[instructionCache[3]] = registers[instructionCache[1]] + registers[instructionCache[2]];
                break;
            case 2: //SUB r1-r2=r3
                registers[instructionCache[3]] = registers[instructionCache[1]] - registers[instructionCache[2]];
                break;
            case 3: //LD1
                registers[instructionCache[1]] = memory.load(Functions.convertTo24Bit(instructionCache[2],instructionCache[3],instructionCache[4]));
                break;
            case 4: //ST1

                break;
            case 5:

                break;
            case 6:

                break;
        }
        cpuPhase = 0;
    }

    public short loadByte() {
        return memory.load(programCounter);
    }

    public void stopCpu() {
        System.out.println("\nCpu Stopped");
        running = false;
    }

    public void setFlags(int val) {
        flagCarry = false;
        flagZero = false;
        flagSign = false;
        flagOverflow = false;
        if (val==0) {
            flagZero = true;
        }
        if (val<0) {
            flagSign = true;
        }
        if (val>16777215) {
            flagCarry = true;
        }
        if (val>33554431) {
            flagOverflow = true;
        }
    }

}
