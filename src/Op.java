public class Op {
    byte[][] codes = new byte[255][2];


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
        codes[31] = new byte[]{2, 3}; //PSH
        codes[32] = new byte[]{2, 3}; //POP
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
        codes[53] = new byte[]{2, 5}; //INT (Software Interrupt) (sp,pc,fl)
        codes[54] = new byte[]{1, 5}; //RFI
        codes[55] = new byte[]{1, 1}; //
        codes[56] = new byte[]{1, 1}; //
        codes[57] = new byte[]{1, 1}; //
        codes[58] = new byte[]{1, 1}; //
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
        codes[75] = new byte[]{3, 3}; //AND
        codes[76] = new byte[]{3, 3}; //OR
        codes[77] = new byte[]{3, 3}; //XOR
        codes[78] = new byte[]{2, 3}; //NOT
        codes[79] = new byte[]{1, 1}; //

        codes[80] = new byte[]{2, 3}; //CBT8 //byte(r0) to 8 bits (r0-r7)
        codes[81] = new byte[]{2, 3}; //C8TB //8 bits (r0-r7) to byte(r0)
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


    }
}
