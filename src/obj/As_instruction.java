package obj;

public class As_instruction {
    public String name;
    public byte bytes;
    public String[] values;
    public int address;
    public int line;

    public As_instruction(String name, byte bytes, String[] values,int address,int line) {
        this.name = name;
        this.bytes = bytes;
        this.values = values;
        this.address = address;
        this.line = line;

    }
}
