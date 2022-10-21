import static java.lang.Runtime.getRuntime;

public class Memory {
    short[] data = new short[16777216]; //16777216
    boolean[] dataCanUse = new boolean[16777216]; //16777216
    boolean[] dataCanStore = new boolean[16777216]; //16777216

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
            dataCanStore[i] = true;
        }
        //extended ram 65536 - 4259839
        for (int i = extendedRamStart; i <= extendedRamEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = true;
        }
        //vram 10485760 - 11010047
        for (int i = vramStart; i <= vramEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = true;
        }
        //rom 11534336 - 12582911
        for (int i = romStart; i <= romEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = false;
        }
        //user removable storage 12582912 - 13631487
        for (int i = ursStart; i <= ursEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = true;
        }
        //char rom 13631488 - 13633535
        for (int i = charRomStart; i <= charRomEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = false;
        }
        //I/O 16711680 - 16711807
        for (int i = ioStart; i <= ioEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = true;
        }


        //test
        store(4096, (short) 3);
        store(4097, (short) 0); //reg
        store(4098, (short) 0); //hi
        store(4099, (short) 23); //med
        store(4100, (short) 112); //low

        store(4101, (short) 3);
        store(4102, (short) 1); //reg
        store(4103, (short) 0); //hi
        store(4104, (short) 23); //med
        store(4105, (short) 113); //low

        store(4106, (short) 1);
        store(4107, (short) 0); //reg a
        store(4108, (short) 1); //reg b
        store(4109, (short) 2); //reg c

        store(4110, (short) 4);
        store(4111, (short) 2); //reg
        store(4112, (short) 0); //hi
        store(4113, (short) 23); //med
        store(4114, (short) 114); //low

        store(4115, (short) 5);
        store(4116, (short) 4); //reg
        store(4117, (short) 0); //hi
        store(4118, (short) 23); //med
        store(4119, (short) 116); //low

        store(4120, (short) 1);
        store(4121, (short) 4); //reg a
        store(4122, (short) 2); //reg b
        store(4123, (short) 5); //reg c

        store(4124, (short) 6);
        store(4125, (short) 5); //reg
        store(4126, (short) 0); //hi
        store(4127, (short) 23); //med
        store(4128, (short) 118); //low

        store(4129, (short) 5);
        store(4130, (short) 8); //reg
        store(4131, (short) 0); //hi
        store(4132, (short) 23); //med
        store(4133, (short) 118); //low

        store(6000, (short) 10);
        store(6001, (short) 10);

        store(6004, (short) 2);
        store(6005, (short) 25);

        //System.out.println(load(0));
        //System.out.println(dataCanUse[65538]);


        //realMemoryUsage();
    }

    public void realMemoryUsage() {
        long memoryUsage = (getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
        System.out.println(memoryUsage+"MB");
    }

    public void store(int address, short value) {
        if (value>255) {
            value=255;
        }
        if (dataCanUse[address] && dataCanStore[address]) {
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
