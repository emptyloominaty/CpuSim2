public class Device {
    byte id;
    String name;
    Cpu cpu;
    int address;
    short type;
    short busWidth;
    int deviceNumber;
    int queueSize;

    public void interrupt() {
        if (type!=0) {
            if (Memory.load(address)>0) {
                Memory.store(address, (short) 0);
                ping();
            }
            if (Memory.load(address+1)>0) {
                Memory.store(address+1, (short) 0);
                ping2();
            }
            if (Memory.load(address+15)>0) {
                Memory.store(address+15, (short) 0);
                getQueueSize();
            }

        }
    }

    public Device(byte id,int deviceNumber, String name,Cpu cpu,short type,short busWidth,int queueSize) {
        this.id = id;
        this.deviceNumber = deviceNumber;
        this.name = name;
        this.cpu = cpu;
        this.address = 0xFF1000+(id*256);
        this.type = type;
        this.busWidth = busWidth;
        this.queueSize = queueSize;
        System.out.print("Device"+id+" ("+type+"): ");
        System.out.printf("%02X%n",this.address);
    }

    int[] inputQueue = new int[32];
    int[] outputQueue = new int[32];

    public void output(byte address, int value) {
        short[] values = Functions.convertFrom24Bit(value);
        Memory.store(address+20,values[0]);
        Memory.store(address+21,values[1]);
        Memory.store(address+22,values[2]);
        cpu.interrupt((byte) (6+id));
    }

    public void ping() {
        short[] values = Functions.convertFrom24Bit(type);
        Memory.store(address+20, values[0] );
        Memory.store(address+21, values[1] );
        Memory.store(address+22, values[2] );
        cpu.interrupt((byte) (6+id));
    }

    public void ping2() {
        short[] values = Functions.convertFrom24Bit(deviceNumber);
        Memory.store(address+20, values[0] );
        Memory.store(address+21, values[1] );
        Memory.store(address+22, values[2] );
        cpu.interrupt((byte) (6+id));
    }

    public void getQueueSize() {
        short[] values = Functions.convertFrom24Bit(queueSize);
        Memory.store(address+20, values[0] );
        Memory.store(address+21, values[1] );
        Memory.store(address+22, values[2] );
        cpu.interrupt((byte) (6+id));
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
    0x00 Ping (Input) (outputs type to 0x11)
    0x01 Ping (Input) (outputs deviceNumber to 0x11)
    0x02
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
    0x0F Get QueueSize (0x18-0x1F) (Input) (outputs QueueSize to 0x11)

    0x10-0x13 Input (Interrupt)
    0x14-0x17 Output (Interrupt)

    0x18-0x1B Input (No Interrupt) (Queue) //TODO:
    0x1C-0x1F Output (No Interrupt) (Queue) //TODO:

    0x20 - 0xFF Custom
    */


}
