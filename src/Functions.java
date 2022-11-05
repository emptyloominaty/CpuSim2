public class Functions {

    //32-BIT
    public static int convertTo32Bit(short a, short b, short c, short d) {
        return  (a << 24) | (b << 16) | (c << 8) | d;
    }
    public static short[] convertFrom32Bit(int val) {
        int d = val & 0xff;
        int c = (val >> 8) & 0xff;
        int b = (val >> 16) & 0xff;
        int a = (val >> 24) & 0xff;
        return new short[]{(short) a, (short) b, (short) c, (short) d};
    }

    //24-BIT
    public static int convertTo24Bit(short a, short b, short c) {
        return  (a << 16) | (b << 8) | c;
    }
    public static short[] convertFrom24Bit(int val) {
        int c = val & 0xff;
        int b = (val >> 8) & 0xff;
        int a = (val >> 16) & 0xff;
        return new short[]{(short) a, (short) b, (short) c};
    }

    //16-BIT
    public static int convertTo16Bit(short a, short b) {
        return  (a << 8) | b;
    }
    public static short[] convertFrom16Bit(int val) {
        int b = val & 0xff;
        int a = (val >> 8) & 0xff;
        return new short[]{(short) a, (short) b};
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


}
