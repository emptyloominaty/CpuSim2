public class HardDrive extends Device {
    short[] memory;
    public HardDrive(byte id, int deviceNumber, String name, Cpu cpu, short type, short busWidth, int memorySize,int size) {
        super(id, deviceNumber, name, cpu, type, busWidth, memorySize);
        memory = new short[size];
    }

    /*

    0x9A 
    0x9B
    0x9C
    0x9D
    0x9E
    0x9F
     */
}
