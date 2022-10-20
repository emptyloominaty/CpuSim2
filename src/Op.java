public class Op {
    byte[][] codes = new byte[255][2];


    public void main() {//[0]=bytes,[1]=cycles
        codes[0] = new byte[]{1, 1}; //NOP /STOP
        codes[1] = new byte[]{4, 5}; //ADD
        codes[2] = new byte[]{4, 5}; //SUB
        codes[3] = new byte[]{5, 6}; //LD1 (8bit)
        codes[4] = new byte[]{5, 6}; //ST1 (8bit)
        codes[5] = new byte[]{5, 6}; //LD2 (16bit)
        codes[6] = new byte[]{5, 6}; //ST2 (16bit)
        codes[7] = new byte[]{5, 6}; //LD3 (24bit)
        codes[8] = new byte[]{5, 6}; //ST3 (24bit)


    }
}
