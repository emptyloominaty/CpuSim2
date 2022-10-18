public class Cpu {

    Memory memory;

    public Cpu(Memory memory) {
        this.memory = memory;
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
    byte bytesLeft = 0;
    int[] instructionCache = new int[6];
    byte instructionCacheIdx = 0;
    byte ramBus = 3; //bytes per clock

    public void main() {
        switch(cpuPhase) {
            //fetch opcode
            case 0:
                fetchOpCode();
                break;
            //fetch other bytes
            case 1:
                fetchOtherBytes();
                break;
            //execute
            case 2:

                break;
            //execute end
            case 3:
                execute();
                break;

        }

    }

    public void fetchOpCode() {
        int val = loadByte();
        op = (byte) val;
        //TODO: get bytes
        //TODO: get cycles
        instructionCacheIdx = 1;
        instructionCache[0] = val;

        System.out.println("OP:"+val);

        programCounter ++;
        cpuPhase++;
        if (op==0) {
            stopCpu();
        }
    }

    public void fetchOtherBytes() {
        int val = loadByte();
        System.out.println("Val"+instructionCacheIdx+": "+val);
        instructionCache[instructionCacheIdx] = val;
        instructionCacheIdx++;
    }

    public void execute() {

    }

    public short loadByte() {
        return memory.load(programCounter);
    }

    public void stopCpu() {
        System.out.println("Cpu Stopped");
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
