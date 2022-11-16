public class Gpu extends Device {
    public Gpu(byte id, int deviceNumber, String name, Cpu cpu, short type, short busWidth, int memorySize) {
        super(id, deviceNumber, name, cpu, type, busWidth, memorySize);
    }
}
