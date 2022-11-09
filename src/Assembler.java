import obj.As_function;
import obj.As_instruction;
import obj.As_var;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Assembler {
    public static void assemble(String code, Op opcodes) {
        Memory.init();
        String[] lines;

        //remove comments ;;
        code = code.replaceAll(";[^:\\r\\n]+(?=;)","");
        code = code.replace(";","");

        lines = code.split("\n");

        As_var[] vars = new As_var[lines.length];
        As_instruction[] instructions = new As_instruction[lines.length];
        As_function[] functions = new As_function[lines.length];

        Map<String, As_function> functionsMap = new HashMap<String, As_function>();
        Map<String, As_var> varsMap = new HashMap<String, As_var>();

        int varIdx = 0;
        int instructionIdx = 0;
        int functionIdx = 0;

        int bytes = 0;

        //System.out.println(Arrays.toString(lines));

        for (int i = 0; i<lines.length; i++) {
            String[] line;
            String name;
            int address = 0;
            short[] values = new short[6];

            line = lines[i].split(" ");

            boolean func = line[0].matches("<.*?>");

            if (Objects.equals(line[0], "VAR") || Objects.equals(line[0], "var")) {
                // VAR 1 name1 25
                // VAR 2 name2 500
                // VAR 3 name3 100000

                String[] chars;
                String val = line[3];
                int val2;
                boolean hex = false;
                name = line[2];
                chars = line[3].split("(?!^)");
                if (chars.length>2) {
                    if (Objects.equals(chars[0], "0") && Objects.equals(chars[1], "x")) {
                        hex = true;
                        val = line[3].substring(2);
                    }
                }
                if (hex) {
                    val2 = Integer.parseInt(val,16);
                } else {
                    val2 = Integer.parseInt(val);
                }

                //System.out.println(name+" - "+val2+" - "+address+" - "+Short.parseShort(line[1]));

                vars[varIdx] = new As_var(name,val2,0, (byte) Short.parseShort(line[1]));
                varsMap.put(name,vars[varIdx]);
                varIdx ++;
            } else if (func) { //function
                String functionName = line[0].replace("<","").replace(">","");
                functions[functionIdx] = new As_function(functionName,i,4096+bytes);
                functionsMap.put(functionName,functions[functionIdx]);
            } else if (!line[0].equals("") && !line[0].equals(" ")) { //instruction
                int idInst = opcodes.names.get(line[0].toUpperCase());
                byte bytesInst = opcodes.codes[idInst][0];
                String[] vals = new String[6];
                if (line.length>6) {
                    vals[5] = line[6];
                }
                if (line.length>5) {
                    vals[4] = line[5];
                }
                if (line.length>4) {
                    vals[3] = line[4];
                }
                if (line.length>3) {
                    vals[2] = line[3];
                }
                if (line.length>2) {
                    vals[1] = line[2];
                }
                if (line.length>1) {
                    vals[0] = line[1];
                }
                instructions[instructionIdx] = new As_instruction(line[0],bytesInst,vals,4096+bytes,i);

                bytes += bytesInst;
                instructionIdx++;
            }


        }

        int varsBytes = 0;
        int varsAddress = 4096+bytes;
        for (int i = 0; i<varIdx; i++) {
            //calc vars addresses
            if (vars[i].address==0) {
                vars[i].address = varsAddress + varsBytes;
                varsBytes += vars[i].bytes;
            }

            //write vars to memory
            short[] store;
            if (vars[i].bytes==1) {
                Memory.store(vars[i].address, (short) vars[i].value);
            } else if (vars[i].bytes==2) {
                store = Functions.convertFrom16Bit(vars[i].value);
                Memory.store(vars[i].address,store[0]);
                Memory.store(vars[i].address+1,store[1]);
            } else if (vars[i].bytes==3) {
                store = Functions.convertFrom24Bit(vars[i].value);
                Memory.store(vars[i].address,store[0]);
                Memory.store(vars[i].address+1,store[1]);
                Memory.store(vars[i].address+2,store[2]);
            }

        }
        //write instruction to memory
        for (int i = 0; i<instructionIdx; i++) {
            byte instructionBytes = instructions[i].bytes;
            String iname = instructions[i].name.toUpperCase();
            int opCode = opcodes.names.get(iname);

            Memory.store(instructions[i].address, (short) opCode);

            //-------------------------------------------------------------------
            if (iname.equals("STAIP")) {
                short[] values;

                String val = instructions[i].values[1];
                int val2 = 0;
                String[] chars;
                boolean hex = false;
                chars = instructions[i].values[1].split("(?!^)");
                if (chars.length>2) {
                    if (Objects.equals(chars[0], "0") && Objects.equals(chars[1], "x")) {
                        hex = true;
                        val = instructions[i].values[1].substring(2);
                    }
                }
                if (hex) {
                    val2 = Integer.parseInt(val,16);
                } else if (Functions.isNumeric(instructions[i].values[1])) {
                    val2 = Integer.parseInt(val);
                }

                Memory.store(instructions[i].address+1, Short.parseShort(instructions[i].values[0]));
                if (Functions.isNumeric(instructions[i].values[1]) || hex) {
                    values = Functions.convertFrom24Bit(val2);
                } else {
                    values = Functions.convertFrom24Bit(functionsMap.get(instructions[i].values[1]).address);
                }
                Memory.store(instructions[i].address+2, values[0]);
                Memory.store(instructions[i].address+3, values[1]);
                Memory.store(instructions[i].address+4, values[2]);
                //-------------------------------------------------------------------
            } else if (iname.equals("INT")) {
                Memory.store(instructions[i].address+1, Short.parseShort(instructions[i].values[0]));
                //-------------------------------------------------------------------
            } else if (iname.equals("LDI1") || iname.equals("LDI2") || iname.equals("LDI3") || iname.equals("ADDI1") ||
                    iname.equals("ADDI2") || iname.equals("ADDI3") || iname.equals("MULI1") || iname.equals("DIVI1") ||
                    iname.equals("SUBI1") || iname.equals("SUBI2") || iname.equals("SUBI3")) {
                Memory.store(instructions[i].address+1, Short.parseShort(removeRfromCode(instructions[i].values[0])));
                if (iname.equals("LDI2") || iname.equals("ADDI2") || iname.equals("SUBI2")) {
                    short[] values = Functions.convertFrom16Bit(Integer.parseInt(instructions[i].values[1]));
                    Memory.store(instructions[i].address+2, values[0]);
                    Memory.store(instructions[i].address+3, values[1]);
                } else if (iname.equals("LDI3") || iname.equals("ADDI3") || iname.equals("SUBI3")) {
                    short[] values = Functions.convertFrom24Bit(Integer.parseInt(instructions[i].values[1]));
                    Memory.store(instructions[i].address+2, values[0]);
                    Memory.store(instructions[i].address+3, values[1]);
                    Memory.store(instructions[i].address+4, values[2]);
                } else {
                    Memory.store(instructions[i].address+2, Short.parseShort(instructions[i].values[1]));
                }
                //-------------------------------------------------------------------
            } else if (iname.equals("LDR1") || iname.equals("LDR2") || iname.equals("LDR3") ||
                    iname.equals("STR1") || iname.equals("STR2") || iname.equals("STR3")) {
                Memory.store(instructions[i].address+1, Short.parseShort(removeRfromCode(instructions[i].values[0])));
                Memory.store(instructions[i].address+2, Short.parseShort(removeRfromCode(instructions[i].values[1])));
                //-------------------------------------------------------------------
            } else {
                for(int j = 0; j < instructions[i].values.length; j++) {
                    int k = j+1; //1
                    if (instructions[i].values[j] != null) {
                        if (findRinCode(instructions[i].values[j])) {
                            //REGISTER
                            Memory.store(instructions[i].address+k, Short.parseShort(removeRfromCode(instructions[i].values[j])));
                        }  else if (Functions.isNumeric(instructions[i].values[j])) {
                            //????
                            Memory.store(instructions[i].address+1, Short.parseShort(instructions[i].values[0]));
                        } else if (!(iname.equals("JSR") || iname.equals("JG") || iname.equals("JNG") || iname.equals("JL") || iname.equals("JNL")
                                || iname.equals("JC") || iname.equals("JNC") || iname.equals("JE") || iname.equals("JNE") || iname.equals("JMP"))) {
                            //VAR MEM ADDRESS
                            short[] varAddress = Functions.convertFrom24Bit(varsMap.get(instructions[i].values[j]).address);
                            Memory.store(instructions[i].address+k, varAddress[0]);
                            Memory.store(instructions[i].address+k+1, varAddress[1]);
                            Memory.store(instructions[i].address+k+2, varAddress[2]);
                        } else {
                            //FUNCTIONS (JUMPS)
                            short[] jumpAddress = Functions.convertFrom24Bit(functionsMap.get(instructions[i].values[j]).address);
                            Memory.store(instructions[i].address+k, jumpAddress[0]);
                            Memory.store(instructions[i].address+k+1, jumpAddress[1]);
                            Memory.store(instructions[i].address+k+2, jumpAddress[2]);
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        /*for (int i = 0; i<instructionIdx;i++) {
            System.out.println(instructions[i].name+" - "+instructions[i].values[0]+" - "+instructions[i].values[1]+" - "+instructions[i].values[2]);
        }*/

        int a = 0;
        int b = 0;
        while(a<256) {
            System.out.print(Memory.data[4096+a]+" ");
            a++;
            b++;
            if (b>16) {
                b = 0;
                System.out.println("");
            }
        }
    }

    public static String removeRfromCode(String string) {
        boolean found = string.matches("\\b([R-R-r-r][0-9]{1,2})");
        if (found) {
            return string.substring(1);
        }
        return string;
    }

    public static boolean findRinCode(String string) {
        return string.matches("\\b([R-R-r-r][0-9]{1,2})");
    }



    public static void loadMachineCode(String code) {
        Memory.init();
        String[] bytes;
        Short[] bytes2 = new Short[4194304];
        code = code.replace("\n","");
        bytes = code.split(" ");
        //System.out.println(Arrays.toString(bytes));
        for (int i = 0; i<bytes.length; i++) {
            bytes2[i] = Short.parseShort(bytes[i], 16);
            Memory.store(i+4096, bytes2[i]);
        }

    }
}