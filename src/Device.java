public class Device {
    byte id;
    String name;
    Cpu cpu;
    int address;
    short type;
    short busWidth;
    int deviceNumber;
    short[] deviceMemory;
    int deviceMemoryPointer = 0;
    int[] in16 = new int[16];
    int[] out16 = new int[16];

    public void interrupt() {
        if (type!=0) {
            if (Memory.load(address)>0) {
                Memory.store(address, (short) 0);
                output(type);
            }
            if (Memory.load(address+1)>0) {
                Memory.store(address+1, (short) 0);
                output(deviceNumber);
            }
            if (Memory.load(address+2)>0) {
                testI = Memory.load(address+2) * 1024;
                testingSpeed = true;
                testVal = 0;
                Memory.store(address+2, (short) 0);
            }
            if (Memory.load(address+3)>0) {
                testI = Memory.load(address+3) * 256;
                testingSpeed16 = true;
                testVal = 0;
                Memory.store(address+3, (short) 0);
            }
            if (testingSpeed) {
                test();
            } else if (testingSpeed16) {
                test16();
            }
        }
    }

    public void store(short value) {
        deviceMemory[deviceMemoryPointer] = value;
        deviceMemoryPointer++;
        if (deviceMemoryPointer>=deviceMemory.length) {
            deviceMemoryPointer = 0;
        }
    }

    public Device(byte id,int deviceNumber, String name,Cpu cpu,short type,short busWidth, int memorySize) {
        this.id = id;
        this.deviceNumber = deviceNumber;
        this.name = name;
        this.cpu = cpu;
        this.address = 0xFF1000+(id*256);
        this.type = type;
        this.busWidth = busWidth;
        deviceMemory = new short[memorySize];
        System.out.print("Device"+id+" ("+type+"): ");
        System.out.printf("%02X%n",this.address);
    }

    public void output(int value) {
        short[] values = Functions.convertFrom24Bit(value);
        Memory.store(address+20,values[0]);
        Memory.store(address+21,values[1]);
        Memory.store(address+22,values[2]);
        Memory.store(address+0x0C, (short) 0);
        cpu.interrupt((byte) (6+id));
    }

    public void output16(short[] value) {
        for (int i = 0; i<16; i++) {
            Memory.store(address+0x30+i,value[i]);
        }
        Memory.store(address+0x0E, (short) 0);
        cpu.interrupt((byte) (28+id));
    }

    int testI = 256;
    int testVal = 0;
    boolean testingSpeed = false;
    public void test() {
        testVal++;
        testI--;
        output(testVal);
        if (testI<=0) {
            testingSpeed = false;
        }
    }
    boolean testingSpeed16 = false;
    public void test16() {
        if (Memory.load(address+0x0E)==0) {
            return;
        }
        testVal++;
        short[] testArray = new short[16];
        for (int i = 0; i<testArray.length; i++) {
            testArray[i] = (short) (testVal+i);
        }
        testI--;
        output16(testArray);
        if (testI<=0) {
            testingSpeed16 = false;
        }
    }

    /*
    Types:
    0 - None
    1 - GPU
    2 - Hard drive
    3 - Network Card

    255 - Test Device
     */

    /*
    0x00 Ping (Input) (outputs type to 0x14)
    0x01 Ping (Input) (outputs deviceNumber to 0x14)
    0x02 speedTest (Input) (1024 x val)
    0x03 speedTest16 (Input) (256 x val)
    0x04
    0x05
    0x06
    0x07
    0x08
    0x09
    0x0A
    0x0B
    0x0C cpuReady //TODO:
    0x0D deviceReady //TODO:
    0x0E cpuReady16
    0x0F deviceReady16

    0x10-0x13 Input (Interrupt)
    0x14-0x17 Output (Interrupt)

    0x18-0x1B Input (No Interrupt) (Queue)
    0x1C-0x1F Output (No Interrupt) (Queue)

    0x20-0x2F 16Byte Input (Interrupt_2)
    0x30-0x3F 16Byte Output (Interrupt_2)

    0x40-0xFF Custom
    */


}
