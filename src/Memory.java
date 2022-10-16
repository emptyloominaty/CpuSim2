public class Memory {
    static short[] data = new short[16777216];
    static boolean[] dataCanUse = new boolean[16777216];

    public static void init() {
        //main ram 0-65535
        for (int i = 0; i < 65536; i++) {
            dataCanUse[i] = true;
        }
        //extended ram 65536 - 4259839
        for (int i = 65536; i < 4259840; i++) {
            dataCanUse[i] = true;
        }
        //vram 10485760 - 11010047
        for (int i = 10485760; i < 11010048; i++) {
            dataCanUse[i] = true;
        }
        //rom 11534336 - 12582911
        for (int i = 11534336; i < 12582912; i++) {
            dataCanUse[i] = true;
        }
        //user removable storage 12582912 - 13631487
        for (int i = 12582912; i < 13631488; i++) {
            dataCanUse[i] = true;
        }
        //char rom 13631488 - 13633535
        for (int i = 13631488; i < 13633536; i++) {
            dataCanUse[i] = true;
        }
        //I/O 16711680 - 16711807
        for (int i = 16711680; i < 16711808; i++) {
            dataCanUse[i] = true;
        }


        //test
        store(0, (short) 5);
        System.out.println(load(0));
        //System.out.println(dataCanUse[65538]);
    }

    public static void store(int address, short value) {
        if (dataCanUse[address]) {
            data[address] = value;
        }
    }

    public static short load(int address) {
        if (dataCanUse[address]) {
            return data[address];
        } else {
            return 0;
        }

    }



}
