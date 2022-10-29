import java.util.Arrays;

public class Assembler {
    public static void assemble(String code) {
        Memory.init();
        //System.out.println(memText.getText());
        obj.As_var[] vars = new obj.As_var[65536];
        obj.As_instruction[] instructions = new obj.As_instruction[262144];

        String[] lines;

        //remove comments ;;
        code = code.replaceAll(";[^:\\r\\n]+(?=;)","");
        code = code.replace(";","");

        lines = code.split("\n");


        System.out.println(Arrays.toString(lines));




        //Memory.realMemoryUsage();
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