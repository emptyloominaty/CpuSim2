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
    int[] instructionCache = new int[6];
    byte instructionCacheIdx = 0;
    byte ramBus = 3; //bytes per clock

    public void main() {
        memThisCycleLeft = 3;
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
                if (cyclesI>0) {
                    cyclesI--;
                }
                if (cyclesI<=0) {
                    cpuPhase++;
                }
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
        bytes = opCodes.codes[val][0];
        bytesLeft = (byte) (bytes-1);
        cycles = opCodes.codes[val][1];
        cyclesI = (byte) (cycles-1);
        instructionCacheIdx = 1;
        memThisCycleLeft = 2;
        instructionCache[0] = val;

        System.out.println("OP:"+val+" B:"+bytes+" C:"+cycles);

        programCounter ++;
        cpuPhase++;
        if (op==0) {
            stopCpu();
        }
    }

    public void fetchOtherBytes() {
        while (memThisCycleLeft>0 && bytesLeft>0) {
            int val = loadByte();
            memThisCycleLeft --;
            System.out.println("Val"+instructionCacheIdx+": "+val);
            instructionCache[instructionCacheIdx] = val;
            instructionCacheIdx++;
            programCounter ++;
            bytesLeft--;
        }
        if (bytesLeft<=0) {
            cpuPhase++;
        }
    }

    public void execute() {
        //TODO:
        switch(op) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

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
