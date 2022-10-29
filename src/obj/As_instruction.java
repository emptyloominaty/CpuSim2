package obj;

public class As_instruction {
    public String name;
    public byte bytes;
    public short[] values = new short[6];

    public As_instruction(String name, byte bytes, short[] values) {
        this.name = name;
        this.bytes = bytes;
        this.values = values;

    }
}
