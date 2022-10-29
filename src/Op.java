public class Op {
    byte[][] codes = new byte[255][2];
    String[] names = new String[255];

    public void main() {//[0]=bytes,[1]=cycles
        codes[0] = new byte[]{1, 1}; //STOP
        codes[1] = new byte[]{4, 4}; //ADD
        codes[2] = new byte[]{4, 4}; //SUB
        codes[3] = new byte[]{5, 6}; //LD1 (8bit)
        codes[4] = new byte[]{5, 6}; //ST1 (8bit)
        codes[5] = new byte[]{5, 6}; //LD2 (16bit)
        codes[6] = new byte[]{5, 6}; //ST2 (16bit)
        codes[7] = new byte[]{5, 6}; //LD3 (24bit)
        codes[8] = new byte[]{5, 6}; //ST3 (24bit)
        codes[9] = new byte[]{3, 3}; //LDI1 (8bit)
        codes[10] = new byte[]{4, 3}; //LDI2 (16bit)
        codes[11] = new byte[]{5, 4}; //LDI3 (24bit)

        codes[12] = new byte[]{2, 3}; //INC
        codes[13] = new byte[]{2, 3}; //DEC
        codes[14] = new byte[]{4, 24}; //MUL
        codes[15] = new byte[]{4, 34}; //DIV
        codes[16] = new byte[]{4, 34}; //DIVR (Get Remainder of Division)
        codes[17] = new byte[]{4, 5}; //ADC
        codes[18] = new byte[]{4, 5}; //SUC
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
        codes[31] = new byte[]{2, 3}; //PSH1
        codes[32] = new byte[]{2, 3}; //POP1
        codes[33] = new byte[]{3, 4}; //TRR (Register to Register)
        codes[34] = new byte[]{2, 4}; //TRP (Register to Program Counter)
        codes[35] = new byte[]{2, 4}; //TPR (Program Counter to Register)
        codes[36] = new byte[]{2, 4}; //TRS (Register to Stack Pointer)
        codes[37] = new byte[]{2, 4}; //TSR (Stack Pointer to Register
        codes[38] = new byte[]{2, 4}; //TCR (Carry Flag to Register)
        codes[39] = new byte[]{2, 4}; //TRC (Register to Carry Flag)

        codes[40] = new byte[]{3, 4}; //AD2 (A=A+B)
        codes[41] = new byte[]{3, 4}; //SU2 (A=A-B)
        codes[42] = new byte[]{3, 4}; //ADDI1
        codes[43] = new byte[]{3, 4}; //SUBI1 (r-val = r)
        codes[44] = new byte[]{3, 23}; //MULI1
        codes[45] = new byte[]{3, 33}; //DIVI1  (r/val = r)
        codes[46] = new byte[]{4, 4}; //ADDI2
        codes[47] = new byte[]{4, 4}; //SUBI2
        codes[48] = new byte[]{5, 5}; //ADDI3
        codes[49] = new byte[]{5, 5}; //SUBI3

        codes[50] = new byte[]{5, 4}; //STAIP (Store Address to Interrupt Pointer)
        codes[51] = new byte[]{1, 2}; //SEI (Enable Interrupts)
        codes[52] = new byte[]{1, 2}; //SDI (Disable Interrupts)
        codes[53] = new byte[]{2, 5}; //INT (Software Interrupt) (pc,fl)
        codes[54] = new byte[]{1, 5}; //RFI
        codes[55] = new byte[]{2, 3}; //PSH2
        codes[56] = new byte[]{2, 3}; //POP2
        codes[57] = new byte[]{2, 3}; //PSH3
        codes[58] = new byte[]{2, 3}; //POP3
        codes[59] = new byte[]{1, 1}; //

        codes[60] = new byte[]{3, 4}; //LDR1 (Load from Address stored in Register) 8bit
        codes[61] = new byte[]{3, 4}; //STR1 (Store to Address stored in Register) 8bit
        codes[62] = new byte[]{3, 4}; //LDR2 (Load from Address stored in Register) 16bit
        codes[63] = new byte[]{3, 4}; //STR2 (Store to Address stored in Register) 16bit
        codes[64] = new byte[]{3, 4}; //LDR3 (Load from Address stored in Register) 24bit
        codes[65] = new byte[]{3, 4}; //STR3 (Store to Address stored in Register) 24bit
        codes[66] = new byte[]{1, 1}; //
        codes[67] = new byte[]{1, 1}; //
        codes[68] = new byte[]{1, 1}; //
        codes[69] = new byte[]{1, 1}; //

        codes[70] = new byte[]{2, 3}; //ROL (Rotate Left)
        codes[71] = new byte[]{2, 3}; //ROR (Rotate Right)
        codes[72] = new byte[]{2, 3}; //SLL (Shift Logical Left)
        codes[73] = new byte[]{2, 3}; //SLR (Shift Logical Right)
        codes[74] = new byte[]{2, 3}; //SRR (Shift Arithmetic Right)
        codes[75] = new byte[]{4, 3}; //AND
        codes[76] = new byte[]{4, 3}; //OR
        codes[77] = new byte[]{4, 3}; //XOR
        codes[78] = new byte[]{2, 3}; //NOT
        codes[79] = new byte[]{1, 1}; //

        codes[80] = new byte[]{2, 4}; //CBT8 //byte(r0) to 8 bits (r0-r7)
        codes[81] = new byte[]{2, 4}; //C8TB //8 bits (r0-r7) to byte(r0)
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

        //--------------------------------------------------
        names[0] = "STOP"; //STOP
        names[1] = "ADD"; //ADD
        names[2] = "SUB"; //SUB
        names[3] = "LD1"; //LD1 (8bit)
        names[4] = "ST1"; //ST1 (8bit)
        names[5] = "LD2"; //LD2 (16bit)
        names[6] = "ST2"; //ST2 (16bit)
        names[7] = "LD3"; //LD3 (24bit)
        names[8] = "ST3"; //ST3 (24bit)
        names[9] = "LDI1"; //LDI1 (8bit)
        names[10] = "LDI2"; //LDI2 (16bit)
        names[11] = "LDI3"; //LDI3 (24bit)

        names[12] = "INC"; //INC
        names[13] = "DEC"; //DEC
        names[14] = "MUL"; //MUL
        names[15] = "DIV"; //DIV
        names[16] = "DIVR"; //DIVR (Get Remainder of Division)
        names[17] = "ADC"; //ADC
        names[18] = "SUC"; //SUC
        names[19] = "NOP"; //NOP

        names[20] = "JMP"; //JMP
        names[21] = "JSR"; //JSR
        names[22] = "RFS"; //RFS
        names[23] = "JG"; //JG (Greater)
        names[24] = "JL"; //JL (Less)
        names[25] = "JNG"; //JNG (Not Greater)
        names[26] = "JNL"; //JNL (Not Less)
        names[27] = "JE"; //JE (Equal)
        names[28] = "JNE"; //JNE (Not Equal)
        names[29] = "JC"; //JC (Carry)

        names[30] = "JNC"; //JNC (Not Carry)
        names[31] = "PHS1"; //PSH1
        names[32] = "POP1"; //POP1
        names[33] = "TRR"; //TRR (Register to Register)
        names[34] = "TRP"; //TRP (Register to Program Counter)
        names[35] = "TPR"; //TPR (Program Counter to Register)
        names[36] = "TRS"; //TRS (Register to Stack Pointer)
        names[37] = "TSR"; //TSR (Stack Pointer to Register
        names[38] = "TCR"; //TCR (Carry Flag to Register)
        names[39] = "TRC"; //TRC (Register to Carry Flag)

        names[40] = "AD2"; //AD2 (A=A+B)
        names[41] = "SU2"; //SU2 (A=A-B)
        names[42] = "ADDI1"; //ADDI1
        names[43] = "SUBI1"; //SUBI1 (r-val = r)
        names[44] = "MULI1"; //MULI1
        names[45] = "DIVI1"; //DIVI1  (r/val = r)
        names[46] = "ADDI2"; //ADDI2
        names[47] = "SUBI2"; //SUBI2
        names[48] = "ADDI3"; //ADDI3
        names[49] = "SUBI3"; //SUBI3

        names[50] = "STAIP"; //STAIP (Store Address to Interrupt Pointer)
        names[51] = "SEI"; //SEI (Enable Interrupts)
        names[52] = "SDI"; //SDI (Disable Interrupts)
        names[53] = "INT"; //INT (Software Interrupt) (pc,fl)
        names[54] = "RFI"; //RFI
        names[55] = "PSH2"; //PSH2
        names[56] = "POP2"; //POP2
        names[57] = "PSH3"; //PSH3
        names[58] = "POP3"; //POP3
        names[59] = ""; //

        names[60] = "LDR1"; //LDR1 (Load from Address stored in Register) 8bit
        names[61] = "STR1"; //STR1 (Store to Address stored in Register) 8bit
        names[62] = "LDR2"; //LDR2 (Load from Address stored in Register) 16bit
        names[63] = "STR2"; //STR2 (Store to Address stored in Register) 16bit
        names[64] = "LDR3"; //LDR3 (Load from Address stored in Register) 24bit
        names[65] = "STR3"; //STR3 (Store to Address stored in Register) 24bit
        names[66] = ""; //
        names[67] = ""; //
        names[68] = ""; //
        names[69] = ""; //

        names[70] = "ROL"; //ROL (Rotate Left)
        names[71] = "ROR"; //ROR (Rotate Right)
        names[72] = "SLL"; //SLL (Shift Logical Left)
        names[73] = "SLR"; //SLR (Shift Logical Right)
        names[74] = "SRR"; //SRR (Shift Arithmetic Right)
        names[75] = "AND"; //AND
        names[76] = "OR"; //OR
        names[77] = "XOR"; //XOR
        names[78] = "NOT"; //NOT
        names[79] = ""; //

        names[80] = "CBT8"; //CBT8 //String(r0) to 8 bits (r0-r7)
        names[81] = "C8TB"; //C8TB //8 bits (r0-r7) to String(r0)
        names[82] = ""; //
        names[83] = ""; //
        names[84] = ""; //
        names[85] = ""; //
        names[86] = ""; //
        names[87] = ""; //
        names[88] = ""; //
        names[89] = ""; //

        names[90] = ""; //
        names[91] = ""; //
        names[92] = ""; //
        names[93] = ""; //
        names[94] = ""; //
        names[95] = ""; //
        names[96] = ""; //
        names[97] = ""; //
        names[98] = ""; //
        names[99] = ""; //

        names[100] = ""; //
        names[101] = ""; //
        names[102] = ""; //
        names[103] = ""; //
        names[104] = ""; //
        names[105] = ""; //
        names[106] = ""; //
        names[107] = ""; //
        names[108] = ""; //
        names[109] = ""; //


    }
}
