public class Op {
    byte[][] codes = new byte[255][2];


    public void main() {//[0]=bytes,[1]=cycles
        codes[0] = new byte[]{1, 1}; //NOP
        codes[1] = new byte[]{4, 5}; //ADD
        codes[2] = new byte[]{4, 5}; //SUB
        codes[3] = new byte[]{5, 6}; //LD
        codes[4] = new byte[]{5, 6}; //ST

    }
}
