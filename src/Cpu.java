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
    long cyclesDone = 0;
    long instructionsDone = 0;
    //int programCounterMax = 4259839;
    //short stackPointerMax = 4095;
    byte cpuPhase = 0;
    byte op = 0;
    byte bytes = 0;
    byte cycles = 0;
    byte cyclesI = 0;
    byte bytesLeft = 0;
    byte memThisCycleLeft = 3;
    short[] instructionData = new short[6];
    byte instructionDataIdx = 0;

    public void main() {
        if (!running) {
            return;
        }
        cyclesDone++;
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

        System.out.print(programCounter+": "+val);

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
        instructionsDone++;
        short[] store;
        short[] bytes;
        int load;
        int val;
        short byte1;
        short byte2;
        short byte3;
        int address1;
        int address2;
        int address3;
        switch(op) {
            case 1: //ADD  r1+r2=r3
                val = registers[instructionData[1]] + registers[instructionData[2]];
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[3]] = val;
                break;
            case 2: //SUB r1-r2=r3
                val = registers[instructionData[1]] - registers[instructionData[2]];
                setFlags(val);
                registers[instructionData[3]] = val;
                break;
            case 3: //LD1
                registers[instructionData[1]] = memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]));
                break;
            case 4: //ST1
                memory.store(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]), (short) registers[instructionData[1]]);
                break;
            case 5: //LD2
                load =  Functions.convertTo16Bit(memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4])),
                    memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3], instructionData[4])+1));
                registers[instructionData[1]] = load;
                break;
            case 6: //ST2
                store = Functions.convertFrom16Bit(registers[instructionData[1]]);
                memory.store(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]),store[0]);
                memory.store(Functions.convertTo24Bit(instructionData[2],instructionData[3], instructionData[4])+1,store[1]);
                break;
            case 7: //LD3
                byte1 = memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]));
                byte2 = memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],(instructionData[4]))+1);
                byte3 = memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],(instructionData[4]))+2);
                load = Functions.convertTo24Bit(byte1,byte2,byte3);
                registers[instructionData[1]] = load;
                break;
            case 8: //ST3
                address1 = Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]);
                address2 = Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4])+1;
                address3 = Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4])+2;
                store = Functions.convertFrom24Bit(registers[instructionData[1]]);
                memory.store(address1,store[0]);
                memory.store(address2,store[1]);
                memory.store(address3,store[2]);
                break;
            case 9: //LDI1
                registers[instructionData[1]] = instructionData[2];
                break;
            case 10: //LDI2
                registers[instructionData[1]] = Functions.convertTo16Bit(instructionData[2],instructionData[3]);
                break;
            case 11: //LDI3
                registers[instructionData[1]] = Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]);
                break;
            case 12: //INC
                registers[instructionData[1]]++;
                setFlags(registers[instructionData[1]]);
                break;
            case 13: //DEC
                registers[instructionData[1]]--;
                setFlags(registers[instructionData[1]]);
                break;
            case 14: //MUL
                val = registers[instructionData[1]] * registers[instructionData[2]];
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[3]] = val;
                break;
            case 15: //DIV
                val = registers[instructionData[1]] / registers[instructionData[2]];
                setFlags(val);
                registers[instructionData[3]] = val;
                break;
            case 16: //DIVR
                val = registers[instructionData[1]] % registers[instructionData[2]];
                setFlags(val);
                registers[instructionData[3]] = val;
                break;
            case 17: //ADC
                val = registers[instructionData[1]] + registers[instructionData[2]];
                if (flagCarry) {
                    val ++;
                }
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[3]] = val;
                break;
            case 18: //SUC
                //TODO:
                break;
            case 19: //NOP
                break;
            case 20: //JMP
                programCounter = Functions.convertTo24Bit(instructionData[1],instructionData[2],instructionData[3]);
                break;
            case 21: //JSR
                bytes = Functions.convertFrom24Bit(programCounter);
                memory.store(stackPointer,bytes[0]);
                stackPointer++;
                memory.store(stackPointer,bytes[1]);
                stackPointer++;
                memory.store(stackPointer,bytes[2]);
                stackPointer++;
                programCounter = Functions.convertTo24Bit(instructionData[1],instructionData[2],instructionData[3]);
                break;
            case 22: //RFS
                stackPointer--;
                byte3 = memory.load(stackPointer);
                stackPointer--;
                byte2 = memory.load(stackPointer);
                stackPointer--;
                byte1 = memory.load(stackPointer);
                programCounter = Functions.convertTo24Bit(byte1,byte2,byte3);
                break;
            case 23: //JG
                if (registers[instructionData[1]]>registers[instructionData[2]]) {
                    programCounter = Functions.convertTo24Bit(instructionData[3],instructionData[4],instructionData[5]);
                }
                break;
            case 24: //JL
                if (registers[instructionData[1]]<registers[instructionData[2]]) {
                    programCounter = Functions.convertTo24Bit(instructionData[3],instructionData[4],instructionData[5]);
                }
                break;
            case 25: //JNG
                if (!(registers[instructionData[1]]>registers[instructionData[2]])) {
                    programCounter = Functions.convertTo24Bit(instructionData[3],instructionData[4],instructionData[5]);
                }
                break;
            case 26: //JNL
                if (!(registers[instructionData[1]]<registers[instructionData[2]])) {
                    programCounter = Functions.convertTo24Bit(instructionData[3],instructionData[4],instructionData[5]);
                }
                break;
            case 27: //JE
                if (registers[instructionData[1]]==registers[instructionData[2]]) {
                    programCounter = Functions.convertTo24Bit(instructionData[3],instructionData[4],instructionData[5]);
                }
                break;
            case 28: //JNE
                if (!(registers[instructionData[1]]==registers[instructionData[2]])) {
                    programCounter = Functions.convertTo24Bit(instructionData[3],instructionData[4],instructionData[5]);
                }
                break;
            case 29: //JC
                if (flagCarry) {
                    programCounter = Functions.convertTo24Bit(instructionData[3],instructionData[4],instructionData[5]);
                    flagCarry = false;
                }
                break;
            case 30: //JNC
                if (!(flagCarry)) {
                    programCounter = Functions.convertTo24Bit(instructionData[3],instructionData[4],instructionData[5]);
                }
                break;
            case 31: //PSH1
                bytes = Functions.convertFrom24Bit(registers[instructionData[1]]);
                memory.store(stackPointer,bytes[2]);
                stackPointer++;
                break;
            case 32: //POP1
                stackPointer--;
                byte3 = memory.load(stackPointer);
                registers[instructionData[1]] = Functions.convertTo24Bit((short) 0, (short) 0,byte3);
                break;
            case 33: //TRR
                registers[instructionData[2]] = registers[instructionData[1]];
                break;
            case 34: //TRP
                programCounter = registers[instructionData[1]];
                break;
            case 35: //TPR
                registers[instructionData[1]] = programCounter;
                break;
            case 36: //TRS
                stackPointer = (short) registers[instructionData[1]];
                break;
            case 37: //TSR
                registers[instructionData[1]] = stackPointer;
                break;
            case 38: //TCR
                flagCarry = registers[instructionData[1]] == 1;
                break;
            case 39: //TRC
                if (flagCarry) {
                    registers[instructionData[1]] = 1;
                } else {
                    registers[instructionData[1]] = 0;
                }
                break;
            case 40: //AD2
                val = registers[instructionData[1]] + registers[instructionData[2]];
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[1]] = val;
                break;
            case 41: //SU2
                val = registers[instructionData[1]] - registers[instructionData[2]];
                setFlags(val);
                registers[instructionData[1]] = val;
                break;
            case 42: //ADDI1
                val = registers[instructionData[1]] + instructionData[2];
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[1]] = val;
                break;
            case 43: //SUBI1
                val = registers[instructionData[1]] - instructionData[2];
                setFlags(val);
                registers[instructionData[1]] = val;
                break;
            case 44: //MULI1
                val = registers[instructionData[1]] * instructionData[2];
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[1]] = val;
                break;
            case 45: //DIVI1
                val = registers[instructionData[1]] / instructionData[2];
                setFlags(val);
                registers[instructionData[1]] = val;
                break;
            case 46: //ADDI2
                val = registers[instructionData[1]] + Functions.convertTo16Bit(instructionData[2],instructionData[3]);
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[1]] = val;
                break;
            case 47: //SUBI2
                val = registers[instructionData[1]] - Functions.convertTo16Bit(instructionData[2],instructionData[3]);
                setFlags(val);
                registers[instructionData[1]] = val;
                break;
            case 48: //ADDI3
                val = registers[instructionData[1]] + Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]);
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[1]] = val;
                break;
            case 49: //SUBI3
                val = registers[instructionData[1]] - Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]);
                setFlags(val);
                registers[instructionData[1]] = val;
                break;
            case 50: //STAIP
                interruptPointers[instructionData[1]] = Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]);
                break;
            case 51: //SEI
                flagInterruptDisable = false;
                break;
            case 52: //DEI
                flagInterruptDisable = true;
                break;
            case 53: //INT
                if (!flagInterruptDisable) {
                    bytes = Functions.convertFrom24Bit(programCounter);
                    memory.store(stackPointer,bytes[0]);
                    stackPointer++;
                    memory.store(stackPointer,bytes[1]);
                    stackPointer++;
                    memory.store(stackPointer,bytes[2]);
                    stackPointer++;

                    if (flagCarry) {
                        memory.store(stackPointer,(short) 1);
                    } else {
                        memory.store(stackPointer,(short) 0);
                    }
                    stackPointer++;
                    if (flagZero) {
                        memory.store(stackPointer,(short) 1);
                    } else {
                        memory.store(stackPointer,(short) 0);
                    }
                    stackPointer++;
                    if (flagOverflow) {
                        memory.store(stackPointer,(short) 1);
                    } else {
                        memory.store(stackPointer,(short) 0);
                    }
                    stackPointer++;
                    if (flagSign) {
                        memory.store(stackPointer,(short) 1);
                    } else {
                        memory.store(stackPointer,(short) 0);
                    }
                    stackPointer++;
                    programCounter = interruptPointers[instructionData[1]];
                }
                break;
            case 54: //RFI
                stackPointer--;
                flagSign = memory.load(stackPointer) == 1;
                stackPointer--;
                flagOverflow = memory.load(stackPointer) == 1;
                stackPointer--;
                flagZero = memory.load(stackPointer) == 1;
                stackPointer--;
                flagCarry = memory.load(stackPointer) == 1;
                stackPointer--;
                byte3 = memory.load(stackPointer);
                stackPointer--;
                byte2 = memory.load(stackPointer);
                stackPointer--;
                byte1 = memory.load(stackPointer);
                programCounter = Functions.convertTo24Bit(byte1,byte2,byte3);
                break;
            case 55: //PSH2
                bytes = Functions.convertFrom24Bit(registers[instructionData[1]]);
                memory.store(stackPointer,bytes[1]);
                stackPointer++;
                memory.store(stackPointer,bytes[2]);
                stackPointer++;
                break;
            case 56: //POP2
                byte1 = 0;
                stackPointer--;
                byte3 = memory.load(stackPointer);
                stackPointer--;
                byte2 = memory.load(stackPointer);
                registers[instructionData[1]] = Functions.convertTo24Bit(byte1,byte2,byte3);
                break;
            case 57: //PSH3
                bytes = Functions.convertFrom24Bit(registers[instructionData[1]]);
                memory.store(stackPointer,bytes[0]);
                stackPointer++;
                memory.store(stackPointer,bytes[1]);
                stackPointer++;
                memory.store(stackPointer,bytes[2]);
                stackPointer++;
                break;
            case 58: //POP3
                stackPointer--;
                byte3 = memory.load(stackPointer);
                stackPointer--;
                byte2 = memory.load(stackPointer);
                stackPointer--;
                byte1 = memory.load(stackPointer);
                registers[instructionData[1]] = Functions.convertTo24Bit(byte1,byte2,byte3);
                break;
            case 59: //

                break;
            case 60: //LDR1
                registers[instructionData[1]] = memory.load(registers[instructionData[2]]);
                break;
            case 61: //STR1
                memory.store(registers[instructionData[2]], (short) registers[instructionData[1]]);
                break;
            case 62: //LDR2
                load =  Functions.convertTo16Bit(memory.load(registers[instructionData[2]]),
                        (short) (memory.load(registers[instructionData[2]])+1));
                registers[instructionData[1]] = load;
                break;
            case 63: //STR2
                store = Functions.convertFrom16Bit(registers[instructionData[1]]);
                memory.store(registers[instructionData[2]],store[0]);
                memory.store(registers[instructionData[2]]+1,store[1]);
                break;
            case 64: //LDR3
                load =  Functions.convertTo24Bit(memory.load(registers[instructionData[2]]),
                        (short) (memory.load(registers[instructionData[2]])+1),
                        (short) (memory.load(registers[instructionData[2]])+2));
                registers[instructionData[1]] = load;
                break;
            case 65: //STR3
                store = Functions.convertFrom24Bit(registers[instructionData[1]]);
                memory.store(registers[instructionData[2]],store[0]);
                memory.store(registers[instructionData[2]]+1,store[1]);
                memory.store(registers[instructionData[2]]+2,store[2]);
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
                //TODO:
                break;
            case 71: //ROR
                //TODO:
                break;
            case 72: //SLL
                val = registers[instructionData[1]] << 1;
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[1]] = val;
                break;
            case 73: //SLR
                val = registers[instructionData[1]] >>> 1;
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[1]] = val;
                break;
            case 74: //SRR
                val = registers[instructionData[1]] >> 1;
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[1]] = val;
                break;
            case 75: //AND
                val = registers[instructionData[1]] & registers[instructionData[2]];
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[3]] = val;
                break;
            case 76: //OR
                val = registers[instructionData[1]] | registers[instructionData[2]];
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[3]] = val;
                break;
            case 77: //XOR
                val = registers[instructionData[1]] ^ registers[instructionData[2]];
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[3]] = val;
                break;
            case 78: //NOT
                val = ~registers[instructionData[1]];
                setFlags(val);
                if (flagCarry) {
                    val -= 16777216;
                }
                registers[instructionData[1]] = val;
                break;
            case 79: //

                break;
            case 80: //CBT8
                val = registers[instructionData[1]];
                registers[instructionData[1]] = val & 0x01;
                registers[instructionData[1]+1] = (val >> 1) & 0x01;
                registers[instructionData[1]+2] = (val >> 2) & 0x01;
                registers[instructionData[1]+3] = (val >> 3) & 0x01;
                registers[instructionData[1]+4] = (val >> 4) & 0x01;
                registers[instructionData[1]+5] = (val >> 5) & 0x01;
                registers[instructionData[1]+6] = (val >> 6) & 0x01;
                registers[instructionData[1]+7] = (val >> 7) & 0x01;
                break;
            case 81: //C8TB
                val = registers[instructionData[1]];
                val |= registers[instructionData[1]+1] << 1;
                val |= registers[instructionData[1]+2] << 2;
                val |= registers[instructionData[1]+3] << 3;
                val |= registers[instructionData[1]+4] << 4;
                val |= registers[instructionData[1]+5] << 5;
                val |= registers[instructionData[1]+6] << 6;
                val |= registers[instructionData[1]+7] << 7;
                registers[instructionData[1]] = val;
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
