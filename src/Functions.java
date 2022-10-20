public class Functions {
    public static int convertTo24Bit(short a, short b, short c) {
        return  (a << 16) | (b << 8) | c;
    }

    public static short[] convertFrom24Bit(int val) {
        int c = val & 0xff;
        int b = (val >> 8) & 0xff;
        int a = (val >> 16) & 0xff;
        return new short[]{(short) a, (short) b, (short) c};
    }

}
