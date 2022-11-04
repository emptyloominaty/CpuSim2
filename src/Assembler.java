import obj.As_function;
import obj.As_instruction;
import obj.As_var;

import java.util.Arrays;
import java.util.Objects;

public class Assembler {
    public static void assemble(String code, Op opcodes) {
        Memory.init();
        String[] lines;

        //remove comments ;;
        code = code.replaceAll(";[^:\\r\\n]+(?=;)","");
        code = code.replace(";","");

        lines = code.split("\n");

        As_var[] vars = new As_var[lines.length+16];
        As_instruction[] instructions = new As_instruction[lines.length+16];
        As_function[] functions = new As_function[lines.length];

        int varIdx = 0;
        int instructionIdx = 0;
        int functionIdx = 0;

        int bytes = 0;

        System.out.println(Arrays.toString(lines));

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
                if (Objects.equals(chars[0], "0") && Objects.equals(chars[1], "x")) {
                    hex = true;
                    val = line[3].substring(2);
                }

                if (hex) {
                    val2 = Integer.parseInt(val,16);
                } else {
                    val2 = Integer.parseInt(val);
                }

                System.out.println(name+" - "+val2+" - "+address+" - "+Short.parseShort(line[1]));

                vars[varIdx] = new As_var(name,val2,address, (byte) Short.parseShort(line[1]));
                varIdx ++;
            } else if (func) { //function
                String functionName = line[0].replace("<","");
                functionName = line[0].replace(">","");
                functions[functionIdx] = new As_function(functionName,i,4096+bytes);

                //functions[fnc] = {name:fnc,line:i, memAddress:(stackSize+1)+bytes}


            } else if (!line[0].equals("") && !line[0].equals(" ")) { //instruction
                int idInst = opcodes.names.get(line[0].toUpperCase());
                byte bytesInst = opcodes.codes[idInst][0];
                String[] vals = new String[6];
                if (line.length>6) {
                    vals[5] = line[6];
                } else if (line.length>5) {
                    vals[4] = line[5];
                } else if (line.length>4) {
                    vals[3] = line[4];
                } else if (line.length>3) {
                    vals[2] = line[3];
                } else if (line.length>2) {
                    vals[1] = line[2];
                } else if (line.length>1) {
                    vals[0] = line[1];
                }
                instructions[instructionIdx] = new As_instruction(line[0],bytesInst,vals,0,i);

                bytes += bytesInst;
                instructionIdx++;
            }


        }


        //calc vars addresses

        //write vars to memory

        //write instruction to memory




    }
    public static void loadMachineCode(String code) {
        Memory.init();
        String[] bytes;
        Short[] bytes2 = new Short[262144];
        code = code.replace("\n","");
        bytes = code.split(" ");
        System.out.println(Arrays.toString(bytes));
        for (int i = 0; i<bytes.length; i++) {
            bytes2[i] = Short.parseShort(bytes[i], 16);
            Memory.store(i+4096, bytes2[i]);
        }

    }
}