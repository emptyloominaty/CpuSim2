import static java.lang.Runtime.getRuntime;

public class Memory {
    short[] data = new short[16777216]; //16777216
    boolean[] dataCanUse = new boolean[16777216]; //16777216

    public void init() {
        //memory config
        int ramEnd = 65535;

        int extendedRamStart = 65536;
        int extendedRamEnd = 4259839;

        int vramStart = 10485760;
        int vramEnd = 11010047;

        int romStart = 11534336;
        int romEnd = 12582911;

        int ursStart = 12582912;
        int ursEnd = 13631487;

        int charRomStart = 13631488;
        int charRomEnd = 13633535;

        int ioStart = 16711680;
        int ioEnd = 16711807;


        //main ram 0-65535
        for (int i = 0; i <= ramEnd; i++) {
            dataCanUse[i] = true;
        }
        //extended ram 65536 - 4259839
        for (int i = extendedRamStart; i <= extendedRamEnd; i++) {
            dataCanUse[i] = true;
        }
        //vram 10485760 - 11010047
        for (int i = vramStart; i <= vramEnd; i++) {
            dataCanUse[i] = true;
        }
        //rom 11534336 - 12582911
        for (int i = romStart; i <= romEnd; i++) {
            dataCanUse[i] = true;
        }
        //user removable storage 12582912 - 13631487
        for (int i = ursStart; i <= ursEnd; i++) {
            dataCanUse[i] = true;
        }
        //char rom 13631488 - 13633535
        for (int i = charRomStart; i <= charRomEnd; i++) {
            dataCanUse[i] = true;
        }
        //I/O 16711680 - 16711807
        for (int i = ioStart; i <= ioEnd; i++) {
            dataCanUse[i] = true;
        }


        //test
        store(4096, (short) 1);
        store(4097, (short) 2);

        //System.out.println(load(0));
        //System.out.println(dataCanUse[65538]);


        //realMemoryUsage();
    }

    public void realMemoryUsage() {
        long memoryUsage = (getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
        System.out.println(memoryUsage+"MB");
    }

    public void store(int address, short value) {
        if (dataCanUse[address]) {
            data[address] = value;
        }
    }

    public short load(int address) {
        if (dataCanUse[address]) {
            return data[address];
        } else {
            return 0;
        }

    }



}
