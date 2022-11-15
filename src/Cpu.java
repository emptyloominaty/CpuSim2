public class Cpu extends Thread {
    boolean debug = false;
    int clock = 0;
    long timeStart = System.currentTimeMillis();
    long timeEnd = System.currentTimeMillis();
    long timeA = System.currentTimeMillis();
    long timeB = System.currentTimeMillis();
    long timeC = System.currentTimeMillis();
    long timeD = System.currentTimeMillis();
    long timeE = System.currentTimeMillis();
    long cyclesDoneB = 0;
    //short[] prevInstruction = new short[6];

    boolean threadRunning = true;

    Op opCodes;
    Frame frame;

    public long clockSet = 2000000;
    long timeClockA = System.nanoTime();
    long timeClockB = System.nanoTime();
    boolean maxClock = true;

    public void run() {
        long timeClockD = 0;
        while(threadRunning) {
            long timeClockWait = 1000000000/clockSet;
            while(running) {
                if (maxClock) {
                    this.main();
                } else {
                    timeClockA = System.nanoTime();
                    this.main();
                    timeClockD = 0;
                    while(timeClockD < timeClockWait) {
                        timeClockB = System.nanoTime();
                        timeClockD = timeClockB - timeClockA;
                    }
                }
            }
            try {
                sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void resetCpu() {
        init();
        cyclesDone = 0;
        instructionsDone = 0;
        cpuPhase = 0;
        op = 0;
        bytes = 0;
        cycles = 0;
        cyclesI = 0;
        bytesLeft = 0;
        memThisCycleLeft = 3;
        instructionData = new short[6];
        instructionDataIdx = 0;
        timeA = System.currentTimeMillis();
        timeB = System.currentTimeMillis();
        timeC = System.currentTimeMillis();
        timeD = System.currentTimeMillis();
        timeE = System.currentTimeMillis();
        cyclesDoneA = 0;
        cyclesDoneB = 0;
        timeStart = System.currentTimeMillis();
        waitCycles = 0;
        interruptHw = false;
        halted = false;
        cyclesTotal = 0;
        cyclesExecuting = 0;
    }

    public void sendFrame(Frame frame) {
        this.frame = frame;
    }

    public Cpu(Op opCodes) {
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

    boolean halted = false;

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
    long cyclesDoneA = 0;
    long instructionsDone = 0;

    int cyclesTotal = 0;
    int cyclesExecuting = 0;

    byte cpuPhase = 0;
    byte op = 0;
    byte bytes = 0;
    byte cycles = 0;
    byte cyclesI = 0;
    byte bytesLeft = 0;
    byte memThisCycleLeft = 3;
    short[] instructionData = new short[6];
    byte instructionDataIdx = 0;
    boolean fetchedOpCode = false;
    int waitCycles = 0;
    boolean interruptHw = false;
    int interruptId = 0;


    public void main() {
        if (!running) {
            return;
        };
        cyclesDoneA++;
        memThisCycleLeft = 3;
        fetchedOpCode = false;
        timeA = System.currentTimeMillis();
        if (timeA-timeB>1000) {
            clock = (int) (cyclesDoneA-cyclesDoneB);
            timeB = System.currentTimeMillis();
            cyclesDoneB = cyclesDoneA;
            if (timeA-timeD>5000) {
                cyclesTotal /= 2;
                cyclesExecuting /= 2;
                timeD = System.currentTimeMillis();
            }
        }
        if (timeA-timeC>33.333333333333333333333333333333) {
            frame.update(this);
            timeC = System.currentTimeMillis();
            timeEnd = timeC;
        }
        if (timeA-timeE>5) {
            frame.updateDevices();
            timeE = System.currentTimeMillis();
        }
        cyclesTotal++;
        if (halted) {
            return;
        }
        cyclesExecuting++;
        cyclesDone++;
        switch(cpuPhase) {
            case 0:
                if (waitCycles>0) {
                    waitCycles--;
                    return;
                }
                if (interruptHw) {
                    interruptHw = false;
                    op = 53;
                    instructionData[0] = 53;
                    instructionData[1] = (short) interruptId;
                    cpuPhase = 2;
                    cyclesI = 0;
                    break;
                }
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
        for (byte i = 0; i<instructionData.length;i++) {
            instructionData[i] = 0;
        }

        short val = loadByte();
        op = (byte) val;
        bytes = opCodes.codes[val][0];
        bytesLeft = (byte) (bytes-1);
        cycles = opCodes.codes[val][1];
        cyclesI = (byte) (cycles-1);
        instructionDataIdx = 1;
        memThisCycleLeft = 2;
        instructionData[0] = val;

        debugPrint(programCounter+": "+val,false);
        fetchedOpCode = true;
        programCounter ++;
        cpuPhase++;
        if (bytes==1 && cyclesI == 0) {
            execute();
        } else if (bytes!=1) {
            fetchOtherBytes();
        }
        if (op==0) {
            stopCpu();
        }
    }

    public void fetchOtherBytes() {
        while (memThisCycleLeft>0 && bytesLeft>0) {
            short val = loadByte();
            memThisCycleLeft --;
            debugPrint(" "+val,false);
            instructionData[instructionDataIdx] = val;
            instructionDataIdx++;
            programCounter ++;
            bytesLeft--;
        }
        if (!fetchedOpCode) {
            cyclesI--;
        }
        if (bytesLeft<=0) {
            debugPrint("",true);
            cpuPhase++;
        }
        if (cyclesI == 0) {
            execute();
        }
    }

    public void debugPrint(String text, boolean newLine) {
        if (debug) {
            if (newLine) {
                System.out.println(text);
            } else {
                System.out.print(text);
            }
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
                if (flagCarry) {
                    registers[instructionData[3]] += 16777216;
                }
                break;
            case 3: //LD1
                registers[instructionData[1]] = Memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]));
                break;
            case 4: //ST1
                Memory.store(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]), (short) registers[instructionData[1]]);
                break;
            case 5: //LD2
                load =  Functions.convertTo16Bit(Memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4])),
                    Memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3], instructionData[4])+1));
                registers[instructionData[1]] = load;
                break;
            case 6: //ST2
                store = Functions.convertFrom16Bit(registers[instructionData[1]]);
                Memory.store(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]),store[0]);
                Memory.store(Functions.convertTo24Bit(instructionData[2],instructionData[3], instructionData[4])+1,store[1]);
                break;
            case 7: //LD3
                byte1 = Memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]));
                byte2 = Memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],(instructionData[4]))+1);
                byte3 = Memory.load(Functions.convertTo24Bit(instructionData[2],instructionData[3],(instructionData[4]))+2);
                load = Functions.convertTo24Bit(byte1,byte2,byte3);
                registers[instructionData[1]] = load;
                break;
            case 8: //ST3
                address1 = Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4]);
                address2 = Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4])+1;
                address3 = Functions.convertTo24Bit(instructionData[2],instructionData[3],instructionData[4])+2;
                store = Functions.convertFrom24Bit(registers[instructionData[1]]);
                Memory.store(address1,store[0]);
                Memory.store(address2,store[1]);
                Memory.store(address3,store[2]);
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
                if (flagCarry) {
                    registers[instructionData[1]] -= 16777216;
                }
                break;
            case 13: //DEC
                registers[instructionData[1]]--;
                setFlags(registers[instructionData[1]]);
                if (flagCarry) {
                    registers[instructionData[1]] += 16777216;
                }
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
                if (registers[instructionData[2]]!=0) {
                    val = registers[instructionData[1]] / registers[instructionData[2]];
                } else {
                    val = 0;
                }
                setFlags(val);
                registers[instructionData[3]] = val;
                break;
            case 16: //DIVR
                if (registers[instructionData[2]]!=0) {
                    val = registers[instructionData[1]] % registers[instructionData[2]];
                } else {
                    val = 0;
                }
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
                val = registers[instructionData[1]] - registers[instructionData[2]];
                if (flagCarry) {
                    val --;
                }
                setFlags(val);
                registers[instructionData[3]] = val;
                if (flagCarry) {
                    registers[instructionData[3]] += 16777216;
                }
                break;
            case 19: //NOP
                break;
            case 20: //JMP
                programCounter = Functions.convertTo24Bit(instructionData[1],instructionData[2],instructionData[3]);
                break;
            case 21: //JSR
                bytes = Functions.convertFrom24Bit(programCounter);
                Memory.store(stackPointer,bytes[0]);
                stackPointer++;
                Memory.store(stackPointer,bytes[1]);
                stackPointer++;
                Memory.store(stackPointer,bytes[2]);
                stackPointer++;
                programCounter = Functions.convertTo24Bit(instructionData[1],instructionData[2],instructionData[3]);
                break;
            case 22: //RFS
                stackPointer--;
                byte3 = Memory.load(stackPointer);
                stackPointer--;
                byte2 = Memory.load(stackPointer);
                stackPointer--;
                byte1 = Memory.load(stackPointer);
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
                Memory.store(stackPointer,bytes[2]);
                stackPointer++;
                break;
            case 32: //POP1
                stackPointer--;
                byte3 = Memory.load(stackPointer);
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
                if (flagCarry) {
                    registers[instructionData[1]] += 16777216;
                }
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
                if (flagCarry) {
                    registers[instructionData[1]] += 16777216;
                }
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
                if (registers[instructionData[2]]!=0) {
                    val = registers[instructionData[1]] / instructionData[2];
                } else {
                    val = 0;
                }
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
                if (flagCarry) {
                    registers[instructionData[1]] += 16777216;
                }
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
                if (flagCarry) {
                    registers[instructionData[1]] += 16777216;
                }
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
                    Memory.store(stackPointer,bytes[0]);
                    stackPointer++;
                    Memory.store(stackPointer,bytes[1]);
                    stackPointer++;
                    Memory.store(stackPointer,bytes[2]);
                    stackPointer++;

                    if (flagCarry) {
                        Memory.store(stackPointer,(short) 1);
                    } else {
                        Memory.store(stackPointer,(short) 0);
                    }
                    stackPointer++;
                    if (flagZero) {
                        Memory.store(stackPointer,(short) 1);
                    } else {
                        Memory.store(stackPointer,(short) 0);
                    }
                    stackPointer++;
                    if (flagOverflow) {
                        Memory.store(stackPointer,(short) 1);
                    } else {
                        Memory.store(stackPointer,(short) 0);
                    }
                    stackPointer++;
                    if (flagSign) {
                        Memory.store(stackPointer,(short) 1);
                    } else {
                        Memory.store(stackPointer,(short) 0);
                    }
                    stackPointer++;
                    programCounter = interruptPointers[instructionData[1]];
                }
                break;
            case 54: //RFI
                stackPointer--;
                flagSign = Memory.load(stackPointer) == 1;
                stackPointer--;
                flagOverflow = Memory.load(stackPointer) == 1;
                stackPointer--;
                flagZero = Memory.load(stackPointer) == 1;
                stackPointer--;
                flagCarry = Memory.load(stackPointer) == 1;
                stackPointer--;
                byte3 = Memory.load(stackPointer);
                stackPointer--;
                byte2 = Memory.load(stackPointer);
                stackPointer--;
                byte1 = Memory.load(stackPointer);
                programCounter = Functions.convertTo24Bit(byte1,byte2,byte3);
                break;
            case 55: //PSH2
                bytes = Functions.convertFrom24Bit(registers[instructionData[1]]);
                Memory.store(stackPointer,bytes[1]);
                stackPointer++;
                Memory.store(stackPointer,bytes[2]);
                stackPointer++;
                break;
            case 56: //POP2
                byte1 = 0;
                stackPointer--;
                byte3 = Memory.load(stackPointer);
                stackPointer--;
                byte2 = Memory.load(stackPointer);
                registers[instructionData[1]] = Functions.convertTo24Bit(byte1,byte2,byte3);
                break;
            case 57: //PSH3
                bytes = Functions.convertFrom24Bit(registers[instructionData[1]]);
                Memory.store(stackPointer,bytes[0]);
                stackPointer++;
                Memory.store(stackPointer,bytes[1]);
                stackPointer++;
                Memory.store(stackPointer,bytes[2]);
                stackPointer++;
                break;
            case 58: //POP3
                stackPointer--;
                byte3 = Memory.load(stackPointer);
                stackPointer--;
                byte2 = Memory.load(stackPointer);
                stackPointer--;
                byte1 = Memory.load(stackPointer);
                registers[instructionData[1]] = Functions.convertTo24Bit(byte1,byte2,byte3);
                break;
            case 59: //HLT
                halted = true;
                flagInterruptDisable = false;
                break;
            case 60: //LDR1
                registers[instructionData[1]] = Memory.load(registers[instructionData[2]]);
                break;
            case 61: //STR1
                Memory.store(registers[instructionData[2]], (short) registers[instructionData[1]]);
                break;
            case 62: //LDR2
                load =  Functions.convertTo16Bit(Memory.load(registers[instructionData[2]]),
                        (Memory.load(registers[instructionData[2]]+1)));
                registers[instructionData[1]] = load;
                break;
            case 63: //STR2
                store = Functions.convertFrom16Bit(registers[instructionData[1]]);
                Memory.store(registers[instructionData[2]],store[0]);
                Memory.store(registers[instructionData[2]]+1,store[1]);
                break;
            case 64: //LDR3
                load =  Functions.convertTo24Bit(
                         Memory.load(registers[instructionData[2]]),
                        (Memory.load(registers[instructionData[2]]+1)),
                        (Memory.load(registers[instructionData[2]]+2)));
                registers[instructionData[1]] = load;
                break;
            case 65: //STR3
                store = Functions.convertFrom24Bit(registers[instructionData[1]]);
                Memory.store(registers[instructionData[2]],store[0]);
                Memory.store(registers[instructionData[2]]+1,store[1]);
                Memory.store(registers[instructionData[2]]+2,store[2]);
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

    public void interrupt(byte id) {
        if (!flagInterruptDisable) {
            interruptHw = true;
            interruptId = id;
            waitCycles = 4;
            halted = false;
        }
    }

    public short loadByte() {
        return Memory.load(programCounter);
    }

    public void stopCpu() {
        debugPrint("\nCpu Stopped",true);
        running = false;
        frame.update(this);
    }

    public void setFlags(int val) {
        flagCarry = false;
        flagZero = false;
        flagSign = false;
        flagOverflow = false;
        if (val==0) {
            flagZero = true;
            debugPrint("ZERO",true);
        }
        if (val<0) {
            flagSign = true;
            flagCarry = true; //TODO:?????
            debugPrint("SIGN+CARRY",true);
        }
        if (val>16777215) {
            flagCarry = true;
            debugPrint("CARRY",true);
        }
        if (val>33554431) {
            flagOverflow = true;
            debugPrint("OVERFLOW",true);
        }
    }

}
