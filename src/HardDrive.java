public class HardDrive extends Device {
    short[] memory;
    int addressPointer;
    int task = 0;
    int dataSize = 0;

    public HardDrive(byte id, int deviceNumber, String name, Cpu cpu, short type, short busWidth, int memorySize,int size) {
        super(id, deviceNumber, name, cpu, type, busWidth, memorySize);
        memory = new short[size];
    }

    @Override
    public void interrupt2() {
        short[] data = new short[16];
        String test = "";
        switch(task) {
            case (0):
                break;
            case (1): //load
                if (Memory.load(address+0x0E)==0) { //check if cpu is ready
                    break;
                }
                if (dataSize<=0) {
                    task = 0;
                    break;
                }
                for (int i = 0; i<16; i++) {
                    data[i] = memory[addressPointer+i];
                    //test += data[i]+", ";
                }
                //System.out.println("LD: "+test);
                dataSize-=16;
                addressPointer += 16;
                output16(data);
                break;
            case (2): //store
                if (Memory.load(address+0x0E)==0) { //check if cpu is ready (all data are in 16B)
                    break;
                }
                if (dataSize<=0) {
                    task = 0;
                    break;
                }
                for (int i = 0; i<16; i++) {
                    memory[addressPointer+i] = Memory.load(address+0x30+i);
                    //test += memory[addressPointer+i]+", ";
                }
                //System.out.println("ST: "+test);
                dataSize-=16;
                addressPointer += 16;
                Memory.store(address+0x0F, (short) 1); //device ready
                output(1);
                break;
        }


        if (Memory.load(address+0x9A)>0) {
            Memory.store(address+0x9A, (short) 0);
            //int addressHi = Functions.convertTo24Bit(Memory.load(address+0x20),Memory.load(address+0x21),Memory.load(address+0x22));
            int addressLo = Functions.convertTo24Bit(Memory.load(address+0x23),Memory.load(address+0x24),Memory.load(address+0x25));

            dataSize =  Functions.convertTo24Bit(Memory.load(address+0x26),Memory.load(address+0x27),Memory.load(address+0x28));

            //TODO: long address =
            addressPointer = addressLo;
            task = 1;
        }
        if (Memory.load(address+0x9B)>0) {
            Memory.store(address+0x9B, (short) 0);
            //int addressHi = Functions.convertTo24Bit(Memory.load(address+0x20),Memory.load(address+0x21),Memory.load(address+0x22));
            int addressLo = Functions.convertTo24Bit(Memory.load(address+0x23),Memory.load(address+0x24),Memory.load(address+0x25));

            dataSize =  Functions.convertTo24Bit(Memory.load(address+0x26),Memory.load(address+0x27),Memory.load(address+0x28));

            //TODO: long address =
            addressPointer = addressLo;
            task = 2;
        }

        Memory.store(address+0x0F, (short) 1); //device ready
    }

    /*
    0x9A Load Data in (0x20-0x25 Address) to (0x30-0x3F)  (0x26-0x28) Size of Data in Bytes
    0x9B Store Data to (0x20-0x25 Address) (0x26-0x28) Size of Data in Bytes  *next 16bytes are data to store
    0x9C
    0x9D
    0x9E
    0x9F
     */
}
