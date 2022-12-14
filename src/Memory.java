import static java.lang.Runtime.getRuntime;

public class Memory {
    static short[] data = new short[16777216]; //16777216
    static boolean[] dataCanUse = new boolean[16777216]; //16777216
    static boolean[] dataCanStore = new boolean[16777216]; //16777216
    //memory config
    static int ramEnd = 0x00FFFF;

    static int extendedRamStart = 0x010000;
    static int extendedRamEnd = 0x40FFFF;

    static int vramStart = 0xA00000;
    static int vramEnd = 0xAFFFFF;

    static int romStart = 0x500000;
    static int romEnd = 0x8FFFFF;

    static int ursStart = 0x900000;
    static int ursEnd = 0x9FFFFF;

    static int ioStart = 0xFF0000;
    static int ioEnd = 0xFF17FF;

    static public void init() {
        data = new short[16777216];

        //main ram
        for (int i = 0; i <= ramEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = true;
        }
        //extended ram
        for (int i = extendedRamStart; i <= extendedRamEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = true;
        }
        //vram
        for (int i = vramStart; i <= vramEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = true;
        }
        //rom
        for (int i = romStart; i <= romEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = false;
        }
        //user removable storage
        for (int i = ursStart; i <= ursEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = true;
        }
        //I/O
        for (int i = ioStart; i <= ioEnd; i++) {
            dataCanUse[i] = true;
            dataCanStore[i] = true;
        }

        //VRAM SET
        //color 1B (1/2/3 8/16/24-bit)
        store(0xA00000, (short) 1);
        //width 2B
        store(0xA00001, (short) 2); //2
        store(0xA00002, (short) 128); //128
        //height 2B
        store(0xA00003, (short) 1); //1
        store(0xA00004, (short) 224); //224
        //frameBufferStart 3B
        store(0xA00005, (short) 0x0A);
        store(0xA00006, (short) 0x00);
        store(0xA00007, (short) 0x0A);
        //frameDrawing 1B (0/1) TODO:?
        store(0xA00008, (short) 0);
        //frameFinished 1B (0/1) TODO:?
        store(0xA00009, (short) 1);
    }

    public static void loadTestProgram() {
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

        store(4134, (short) 21);
        store(4135, (short) 0);
        store(4136, (short) 19);
        store(4137, (short) 136);

        store(5000, (short) 1);
        store(5001, (short) 0);
        store(5002, (short) 2);
        store(5003, (short) 3);

        store(5004, (short) 22);

        store(4138, (short) 33);
        store(4139, (short) 3);
        store(4140, (short) 20);

        store(4141, (short) 31);
        store(4142, (short) 20);

        store(4143, (short) 31);
        store(4144, (short) 20);

        store(4145, (short) 31);
        store(4146, (short) 20);

        store(4147, (short) 55);
        store(4148, (short) 20);

        store(4149, (short) 57);
        store(4150, (short) 20);

        store(4151, (short) 31);
        store(4152, (short) 20);

        store(4153, (short) 32);
        store(4154, (short) 21);

        store(4155, (short) 58);
        store(4156, (short) 22);

        store(4157, (short) 56);
        store(4158, (short) 23);

        store(4159, (short) 32);
        store(4160, (short) 24);

        store(4161, (short) 27);
        store(4162, (short) 22);
        store(4163, (short) 23);
        store(4164, (short) 0);
        store(4165, (short) 19);
        store(4166, (short) 141);

        store(5005, (short) 1);
        store(5006, (short) 22);
        store(5007, (short) 23);
        store(5008, (short) 25);

        store(5009, (short) 11);
        store(5010, (short) 26);
        store(5011, (short) 255); //255
        store(5012, (short) 0); //255
        store(5013, (short) 35); //0

        store(5014, (short) 1);
        store(5015, (short) 22);
        store(5016, (short) 23);
        store(5017, (short) 23);

        store(5018, (short) 10);
        store(5019, (short) 27);
        store(5020, (short) 0);
        store(5021, (short) 28);

        store(5022, (short) 2);
        store(5023, (short) 23);
        store(5024, (short) 27);
        store(5025, (short) 23);

        store(5026, (short) 24);
        store(5027, (short) 23);
        store(5028, (short) 26);
        store(5029, (short) 0);
        store(5030, (short) 19);
        store(5031, (short) 150);

        store(5032, (short) 9);
        store(5033, (short) 28);
        store(5034, (short) 1);

        store(5035, (short) 9);
        store(5036, (short) 10);
        store(5037, (short) 24);

        store(5038, (short) 80);
        store(5039, (short) 10);

        store(5040, (short) 81);
        store(5041, (short) 10);

        store(5042, (short) 11);
        store(5043, (short) 29);
        store(5044, (short) 0);
        store(5045, (short) 64);
        store(5046, (short) 0);

        store(5047, (short) 12);
        store(5048, (short) 30);

        store(5049, (short) 24);
        store(5050, (short) 30);
        store(5051, (short) 29);
        store(5052, (short) 0);
        store(5053, (short) 19);
        store(5054, (short) 183);

        store(5055, (short) 9);
        store(5056, (short) 30);
        store(5057, (short) 0);

        store(5058, (short) 12);
        store(5059, (short) 31);

        store(5060, (short) 24);
        store(5061, (short) 31);
        store(5062, (short) 29);
        store(5063, (short) 0);
        store(5064, (short) 19);
        store(5065, (short) 183);

        store(6000, (short) 10);
        store(6001, (short) 10);
        store(6004, (short) 2);
        store(6005, (short) 25);
    }

    public static void realMemoryUsage() {
        long memoryUsage = (getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576;
        System.out.println(memoryUsage+"MB");
    }

    public static void store(int address, short value) {
        if (address<0) {
            address = 0;
        }
        value = (short) (value & 0xFF);
        if (dataCanUse[address] && dataCanStore[address]) {
            data[address] = value;
        }
    }

    public static short load(int address) {
        if (address<0) {//TODO FIX
            address = 0;
        }
        if (dataCanUse[address]) {
            return data[address];
        } else {
            return 0;
        }

    }



}
