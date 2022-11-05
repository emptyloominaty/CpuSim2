import java.util.HashMap;
import java.util.Map;

public class Op {
    byte[][] codes = new byte[255][2];
    String[] names2 = new String[255];
    Map<String, Integer> names = new HashMap<String, Integer>();

    public void main() {//[0]=bytes,[1]=cycles
        codes[0] = new byte[]{1, 1}; //STOP
        codes[1] = new byte[]{4, 4}; //ADD
        codes[2] = new byte[]{4, 4}; //SUB
        codes[3] = new byte[]{5, 4}; //LD1 (8bit)
        codes[4] = new byte[]{5, 4}; //ST1 (8bit)
        codes[5] = new byte[]{5, 4}; //LD2 (16bit)
        codes[6] = new byte[]{5, 4}; //ST2 (16bit)
        codes[7] = new byte[]{5, 4}; //LD3 (24bit)
        codes[8] = new byte[]{5, 4}; //ST3 (24bit)
        codes[9] = new byte[]{3, 2}; //LDI1 (8bit)
        codes[10] = new byte[]{4, 3}; //LDI2 (16bit)
        codes[11] = new byte[]{5, 3}; //LDI3 (24bit)

        codes[12] = new byte[]{2, 3}; //INC
        codes[13] = new byte[]{2, 3}; //DEC
        codes[14] = new byte[]{4, 24}; //MUL
        codes[15] = new byte[]{4, 34}; //DIV
        codes[16] = new byte[]{4, 34}; //DIVR (Get Remainder of Division)
        codes[17] = new byte[]{4, 4}; //ADC
        codes[18] = new byte[]{4, 4}; //SUC
        codes[19] = new byte[]{1, 1}; //NOP

        codes[20] = new byte[]{4, 3}; //JMP
        codes[21] = new byte[]{4, 5}; //JSR
        codes[22] = new byte[]{1, 5}; //RFS
        codes[23] = new byte[]{6, 5}; //JG (Greater)
        codes[24] = new byte[]{6, 5}; //JL (Less)
        codes[25] = new byte[]{6, 5}; //JNG (Not Greater)
        codes[26] = new byte[]{6, 5}; //JNL (Not Less)
        codes[27] = new byte[]{6, 5}; //JE (Equal)
        codes[28] = new byte[]{6, 5}; //JNE (Not Equal)
        codes[29] = new byte[]{4, 4}; //JC (Carry)

        codes[30] = new byte[]{4, 4}; //JNC (Not Carry)
        codes[31] = new byte[]{2, 2}; //PSH1
        codes[32] = new byte[]{2, 2}; //POP1
        codes[33] = new byte[]{3, 3}; //TRR (Register to Register)
        codes[34] = new byte[]{2, 3}; //TRP (Register to Program Counter)
        codes[35] = new byte[]{2, 3}; //TPR (Program Counter to Register)
        codes[36] = new byte[]{2, 3}; //TRS (Register to Stack Pointer)
        codes[37] = new byte[]{2, 3}; //TSR (Stack Pointer to Register
        codes[38] = new byte[]{2, 3}; //TCR (Carry Flag to Register)
        codes[39] = new byte[]{2, 3}; //TRC (Register to Carry Flag)

        codes[40] = new byte[]{3, 3}; //AD2 (A=A+B)
        codes[41] = new byte[]{3, 3}; //SU2 (A=A-B)
        codes[42] = new byte[]{3, 3}; //ADDI1
        codes[43] = new byte[]{3, 3}; //SUBI1 (r-val = r)
        codes[44] = new byte[]{3, 23}; //MULI1
        codes[45] = new byte[]{3, 33}; //DIVI1  (r/val = r)
        codes[46] = new byte[]{4, 4}; //ADDI2
        codes[47] = new byte[]{4, 4}; //SUBI2
        codes[48] = new byte[]{5, 4}; //ADDI3
        codes[49] = new byte[]{5, 4}; //SUBI3

        codes[50] = new byte[]{5, 3}; //STAIP (Store Address to Interrupt Pointer)
        codes[51] = new byte[]{1, 2}; //SEI (Enable Interrupts)
        codes[52] = new byte[]{1, 2}; //SDI (Disable Interrupts)
        codes[53] = new byte[]{2, 5}; //INT (Software Interrupt) (pc,fl)
        codes[54] = new byte[]{1, 5}; //RFI
        codes[55] = new byte[]{2, 2}; //PSH2
        codes[56] = new byte[]{2, 2}; //POP2
        codes[57] = new byte[]{2, 2}; //PSH3
        codes[58] = new byte[]{2, 2}; //POP3
        codes[59] = new byte[]{1, 1}; //

        codes[60] = new byte[]{3, 3}; //LDR1 (Load from Address stored in Register) 8bit
        codes[61] = new byte[]{3, 3}; //STR1 (Store to Address stored in Register) 8bit
        codes[62] = new byte[]{3, 3}; //LDR2 (Load from Address stored in Register) 16bit
        codes[63] = new byte[]{3, 3}; //STR2 (Store to Address stored in Register) 16bit
        codes[64] = new byte[]{3, 3}; //LDR3 (Load from Address stored in Register) 24bit
        codes[65] = new byte[]{3, 3}; //STR3 (Store to Address stored in Register) 24bit
        codes[66] = new byte[]{1, 1}; //
        codes[67] = new byte[]{1, 1}; //
        codes[68] = new byte[]{1, 1}; //
        codes[69] = new byte[]{1, 1}; //

        codes[70] = new byte[]{2, 3}; //ROL (Rotate Left)
        codes[71] = new byte[]{2, 3}; //ROR (Rotate Right)
        codes[72] = new byte[]{2, 3}; //SLL (Shift Logical Left)
        codes[73] = new byte[]{2, 3}; //SLR (Shift Logical Right)
        codes[74] = new byte[]{2, 3}; //SRR (Shift Arithmetic Right)
        codes[75] = new byte[]{4, 4}; //AND
        codes[76] = new byte[]{4, 4}; //OR
        codes[77] = new byte[]{4, 4}; //XOR
        codes[78] = new byte[]{2, 4}; //NOT
        codes[79] = new byte[]{1, 1}; //

        codes[80] = new byte[]{2, 6}; //CBT8 //byte(r0) to 8 bits (r0-r7)
        codes[81] = new byte[]{2, 6}; //C8TB //8 bits (r0-r7) to byte(r0)
        codes[82] = new byte[]{1, 1}; //
        codes[83] = new byte[]{1, 1}; //
        codes[84] = new byte[]{1, 1}; //
        codes[85] = new byte[]{1, 1}; //
        codes[86] = new byte[]{1, 1}; //
        codes[87] = new byte[]{1, 1}; //
        codes[88] = new byte[]{1, 1}; //
        codes[89] = new byte[]{1, 1}; //

        codes[90] = new byte[]{1, 1}; //
        codes[91] = new byte[]{1, 1}; //
        codes[92] = new byte[]{1, 1}; //
        codes[93] = new byte[]{1, 1}; //
        codes[94] = new byte[]{1, 1}; //
        codes[95] = new byte[]{1, 1}; //
        codes[96] = new byte[]{1, 1}; //
        codes[97] = new byte[]{1, 1}; //
        codes[98] = new byte[]{1, 1}; //
        codes[99] = new byte[]{1, 1}; //

        codes[100] = new byte[]{1, 1}; //
        codes[101] = new byte[]{1, 1}; //
        codes[102] = new byte[]{1, 1}; //
        codes[103] = new byte[]{1, 1}; //
        codes[104] = new byte[]{1, 1}; //
        codes[105] = new byte[]{1, 1}; //
        codes[106] = new byte[]{1, 1}; //
        codes[107] = new byte[]{1, 1}; //
        codes[108] = new byte[]{1, 1}; //
        codes[109] = new byte[]{1, 1}; //

        codes[110] = new byte[]{1, 1}; //

        //-------------------------------------------------
        names.put("STOP", 0);
        names.put("ADD", 1);
        names.put("SUB", 2);
        names.put("LD1", 3);
        names.put("ST1", 4);
        names.put("LD2", 5);
        names.put("ST2", 6);
        names.put("LD3", 7);
        names.put("ST3", 8);
        names.put("LDI1", 9);
        names.put("LDI2", 10);
        names.put("LDI3", 11);

        names.put("INC", 12);
        names.put("DEC", 13);
        names.put("MUL", 14);
        names.put("DIV", 15);
        names.put("DIVR", 16);
        names.put("ADC", 17);
        names.put("SUC", 18);
        names.put("NOP", 19);

        names.put("JMP", 20);
        names.put("JSR", 21);
        names.put("RFS", 22);
        names.put("JG", 23);
        names.put("JL", 24);
        names.put("JNG", 25);
        names.put("JNL", 26);
        names.put("JE", 27);
        names.put("JNE", 28);
        names.put("JC", 29);

        names.put("JNC", 30);
        names.put("PHS1", 31);
        names.put("POP1", 32);
        names.put("TRR", 33);
        names.put("TRP", 34);
        names.put("TPR", 35);
        names.put("TRS", 36);
        names.put("TSR", 37);
        names.put("TCR", 38);
        names.put("TRC", 39);

        names.put("AD2", 40);
        names.put("SU2", 41);
        names.put("ADDI1", 42);
        names.put("SUBI1", 43);
        names.put("MULI1", 44);
        names.put("DIVI1", 45);
        names.put("ADDI2", 46);
        names.put("SUBI2", 47);
        names.put("ADDI3", 48);
        names.put("SUBI3", 49);

        names.put("STAIP", 50);
        names.put("SEI", 51);
        names.put("SDI", 52);
        names.put("INT", 53);
        names.put("RFI", 54);
        names.put("PSH2", 55);
        names.put("POP2", 56);
        names.put("PSH3", 57);
        names.put("POP3", 58);
        //names.put("", 59);

        names.put("LDR1", 60);
        names.put("STR1", 61);
        names.put("LDR2", 62);
        names.put("STR2", 63);
        names.put("LDR3", 64);
        names.put("STR3", 65);
        //names.put("", 66);
        //names.put("", 67);
        //names.put("", 68);
        //names.put("", 69);

        names.put("ROL", 70);
        names.put("ROR", 71);
        names.put("SLL", 72);
        names.put("SLR", 73);
        names.put("SRR", 74);
        names.put("AND", 75);
        names.put("OR", 76);
        names.put("XOR", 77);
        names.put("NOT", 78);
        //names.put("", 79);

        names.put("CBT8", 80);
        names.put("C8TB", 81);
        /*names.put("", 82);
        names.put("", 83);
        names.put("", 84);
        names.put("", 85);
        names.put("", 86);
        names.put("", 87);
        names.put("", 88);
        names.put("", 89);

        names.put("", 90);
        names.put("", 91);
        names.put("", 92);
        names.put("", 93);
        names.put("", 94);
        names.put("", 95);
        names.put("", 96);
        names.put("", 97);
        names.put("", 98);
        names.put("", 99);

        names.put("", 100);
        names.put("", 101);
        names.put("", 102);
        names.put("", 103);
        names.put("", 104);
        names.put("", 105);
        names.put("", 106);
        names.put("", 107);
        names.put("", 108);
        names.put("", 109);*/

        //System.out.println(names.get("ADD"));
        //System.out.println(names.get("STOP"));



        //-------------------------------------------------
        names2[0] = "STOP"; //STOP
        names2[1] = "ADD"; //ADD
        names2[2] = "SUB"; //SUB
        names2[3] = "LD1"; //LD1 (8bit)
        names2[4] = "ST1"; //ST1 (8bit)
        names2[5] = "LD2"; //LD2 (16bit)
        names2[6] = "ST2"; //ST2 (16bit)
        names2[7] = "LD3"; //LD3 (24bit)
        names2[8] = "ST3"; //ST3 (24bit)
        names2[9] = "LDI1"; //LDI1 (8bit)
        names2[10] = "LDI2"; //LDI2 (16bit)
        names2[11] = "LDI3"; //LDI3 (24bit)

        names2[12] = "INC"; //INC
        names2[13] = "DEC"; //DEC
        names2[14] = "MUL"; //MUL
        names2[15] = "DIV"; //DIV
        names2[16] = "DIVR"; //DIVR (Get Remainder of Division)
        names2[17] = "ADC"; //ADC
        names2[18] = "SUC"; //SUC
        names2[19] = "NOP"; //NOP

        names2[20] = "JMP"; //JMP
        names2[21] = "JSR"; //JSR
        names2[22] = "RFS"; //RFS
        names2[23] = "JG"; //JG (Greater)
        names2[24] = "JL"; //JL (Less)
        names2[25] = "JNG"; //JNG (Not Greater)
        names2[26] = "JNL"; //JNL (Not Less)
        names2[27] = "JE"; //JE (Equal)
        names2[28] = "JNE"; //JNE (Not Equal)
        names2[29] = "JC"; //JC (Carry)

        names2[30] = "JNC"; //JNC (Not Carry)
        names2[31] = "PHS1"; //PSH1
        names2[32] = "POP1"; //POP1
        names2[33] = "TRR"; //TRR (Register to Register)
        names2[34] = "TRP"; //TRP (Register to Program Counter)
        names2[35] = "TPR"; //TPR (Program Counter to Register)
        names2[36] = "TRS"; //TRS (Register to Stack Pointer)
        names2[37] = "TSR"; //TSR (Stack Pointer to Register
        names2[38] = "TCR"; //TCR (Carry Flag to Register)
        names2[39] = "TRC"; //TRC (Register to Carry Flag)

        names2[40] = "AD2"; //AD2 (A=A+B)
        names2[41] = "SU2"; //SU2 (A=A-B)
        names2[42] = "ADDI1"; //ADDI1
        names2[43] = "SUBI1"; //SUBI1 (r-val = r)
        names2[44] = "MULI1"; //MULI1
        names2[45] = "DIVI1"; //DIVI1  (r/val = r)
        names2[46] = "ADDI2"; //ADDI2
        names2[47] = "SUBI2"; //SUBI2
        names2[48] = "ADDI3"; //ADDI3
        names2[49] = "SUBI3"; //SUBI3

        names2[50] = "STAIP"; //STAIP (Store Address to Interrupt Pointer)
        names2[51] = "SEI"; //SEI (Enable Interrupts)
        names2[52] = "SDI"; //SDI (Disable Interrupts)
        names2[53] = "INT"; //INT (Software Interrupt) (pc,fl)
        names2[54] = "RFI"; //RFI
        names2[55] = "PSH2"; //PSH2
        names2[56] = "POP2"; //POP2
        names2[57] = "PSH3"; //PSH3
        names2[58] = "POP3"; //POP3
        names2[59] = ""; //

        names2[60] = "LDR1"; //LDR1 (Load from Address stored in Register) 8bit
        names2[61] = "STR1"; //STR1 (Store to Address stored in Register) 8bit
        names2[62] = "LDR2"; //LDR2 (Load from Address stored in Register) 16bit
        names2[63] = "STR2"; //STR2 (Store to Address stored in Register) 16bit
        names2[64] = "LDR3"; //LDR3 (Load from Address stored in Register) 24bit
        names2[65] = "STR3"; //STR3 (Store to Address stored in Register) 24bit
        names2[66] = ""; //
        names2[67] = ""; //
        names2[68] = ""; //
        names2[69] = ""; //

        names2[70] = "ROL"; //ROL (Rotate Left)
        names2[71] = "ROR"; //ROR (Rotate Right)
        names2[72] = "SLL"; //SLL (Shift Logical Left)
        names2[73] = "SLR"; //SLR (Shift Logical Right)
        names2[74] = "SRR"; //SRR (Shift Arithmetic Right)
        names2[75] = "AND"; //AND
        names2[76] = "OR"; //OR
        names2[77] = "XOR"; //XOR
        names2[78] = "NOT"; //NOT
        names2[79] = ""; //

        names2[80] = "CBT8"; //CBT8 //String(r0) to 8 bits (r0-r7)
        names2[81] = "C8TB"; //C8TB //8 bits (r0-r7) to String(r0)
        names2[82] = ""; //
        names2[83] = ""; //
        names2[84] = ""; //
        names2[85] = ""; //
        names2[86] = ""; //
        names2[87] = ""; //
        names2[88] = ""; //
        names2[89] = ""; //

        names2[90] = ""; //
        names2[91] = ""; //
        names2[92] = ""; //
        names2[93] = ""; //
        names2[94] = ""; //
        names2[95] = ""; //
        names2[96] = ""; //
        names2[97] = ""; //
        names2[98] = ""; //
        names2[99] = ""; //

        names2[100] = ""; //
        names2[101] = ""; //
        names2[102] = ""; //
        names2[103] = ""; //
        names2[104] = ""; //
        names2[105] = ""; //
        names2[106] = ""; //
        names2[107] = ""; //
        names2[108] = ""; //
        names2[109] = ""; //


    }
}
