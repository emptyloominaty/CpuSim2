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
            if (testingSpeed) {
                test();
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
        cpu.interrupt((byte) (6+id));
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
    0x03
    0x04
    0x05
    0x06
    0x07
    0x08
    0x09
    0x0A
    0x0B
    0x0C
    0x0D
    0x0E
    0x0F

    0x10-0x13 Input (Interrupt)
    0x14-0x17 Output (Interrupt)

    0x18-0x1B Input (No Interrupt) (Queue)
    0x1C-0x1F Output (No Interrupt) (Queue)

    0x20 - 0xFF Custom
    */


}
