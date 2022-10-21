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
    short[] instructionData = new short[6];
    byte instructionDataIdx = 0;
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
                    execute();
                }
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
        instructionDataIdx = 1;
        memThisCycleLeft = 2;
        instructionData[0] = val;

        System.out.print(val);

        programCounter ++;
        cpuPhase++;
        if (bytes==1 && cyclesI == 0) {
            execute();
        }
        if (op==0) {
            stopCpu();
        }
    }

    public void fetchOtherBytes() {
        while (memThisCycleLeft>0 && bytesLeft>0) {
            short val = loadByte();
            memThisCycleLeft --;
            System.out.print(" "+val);
            instructionData[instructionDataIdx] = val;
            instructionDataIdx++;
            programCounter ++;
            bytesLeft--;
        }
        cyclesI--;
        if (bytesLeft<=0) {
            System.out.println("");
            cpuPhase++;
        }
        if (cyclesI == 0) {
            execute();
        }
    }

    public void execute() {
        int val = 1;
        switch(op) {
            case 1: //ADD  r1+r2=r3
                val = registers[instructionData[1]] + registers[instructionData[2]];
                registers[instructionData[3]] = val;
                break;
            case 2: //SUB r1-r2=r3
                val = registers[instructionData[1]] - registers[instructionData[2]];
                registers[instructionData[3]] = val;
                break;
            case 3: //LD1
                registers[instructionData[1]] = memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]));
                break;
            case 4: //ST1
                memory.store(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]), (short) registers[instructionData[1]]);
                break;
            case 5: //LD2
                int load =  Functions.convertTo16Bit(memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4])),
                    memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3], (short) (instructionData[4]+1))));
                registers[instructionData[1]] = load;
                System.out.println("r"+instructionData[1]+": "+registers[instructionData[1]]);
                break;
            case 6: //ST2
                short[] store = Functions.convertFrom16Bit(registers[instructionData[1]]);
                memory.store(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]),store[0]);
                memory.store(Functions.convertTo24Bit(instructionData[2],instructionData[3], (short) (instructionData[4]+1)),store[1]);
                break;
            case 7: //LD3

                break;
            case 8: //ST3

                break;
            case 9: //LDI1

                break;
            case 10: //LDI2

                break;
            case 11: //LDI3

                break;
            case 12: //INC

                break;
            case 13: //DEC

                break;
            case 14: //MUL

                break;
            case 15: //DIV

                break;
            case 16: //DIVR

                break;
            case 17: //ADC

                break;
            case 18: //SUC

                break;
            case 19: //NOP
                break;
            case 20: //JMP

                break;
            case 21: //JSR

                break;
            case 22: //RFS

                break;
            case 23: //JG

                break;
            case 24: //JL

                break;
            case 25: //JNG

                break;
            case 26: //JNL

                break;
            case 27: //JE

                break;
            case 28: //JNE

                break;
            case 29: //JC

                break;
            case 30: //JNC

                break;
            case 31: //PSH

                break;
            case 32: //POP

                break;
            case 33: //TRR

                break;
            case 34: //TRP

                break;
            case 35: //TPR

                break;
            case 36: //TRS

                break;
            case 37: //TSR

                break;
            case 38: //TCR

                break;
            case 39: //TRC

                break;
            case 40: //AD2

                break;
            case 41: //SU2

                break;
            case 42: //ADDI1

                break;
            case 43: //SUBI1

                break;
            case 44: //MULI1

                break;
            case 45: //DIVI1

                break;
            case 46: //ADDI2

                break;
            case 47: //SUBI2

                break;
            case 48: //ADDI3

                break;
            case 49: //SUBI3

                break;
            case 50: //STAIP

                break;
            case 51: //SEI

                break;
            case 52: //DEI

                break;
            case 53: //INT

                break;
            case 54: //RFI

                break;
            case 55: //

                break;
            case 56: //

                break;
            case 57: //

                break;
            case 58: //

                break;
            case 59: //

                break;
            case 60: //LDR1

                break;
            case 61: //STR1

                break;
            case 62: //LDR2

                break;
            case 63: //STR2

                break;
            case 64: //LDR3

                break;
            case 65: //STR3

                break;
            case 66: //

                break;
            case 67: //

                break;
            case 68: //

                break;
            case 69: //

                break;
            case 70: //ROL

                break;
            case 71: //ROR

                break;
            case 72: //SLL

                break;
            case 73: //SLR

                break;
            case 74: //SRR

                break;
            case 75: //AND

                break;
            case 76: //OR

                break;
            case 77: //XOR

                break;
            case 78: //NOT

                break;
            case 79: //

                break;
            case 80: //CBT8

                break;
            case 81: //C8TB

                break;
            case 82: //

                break;
            case 83: //

                break;
            case 84: //

                break;
            case 85: //

                break;
            case 86: //

                break;
            case 87: //

                break;
            case 88: //

                break;
            case 89: //

                break;
        }
        setFlags(val);
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
            System.out.println("ZERO");
        }
        if (val<0) {
            flagSign = true;
            System.out.println("SIGN");
        }
        if (val>16777215) {
            flagCarry = true;
            System.out.println("CARRY");
        }
        if (val>33554431) {
            flagOverflow = true;
            System.out.println("OVERFLOW");
        }
    }

}
