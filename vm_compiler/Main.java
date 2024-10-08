
public class Main{

    public static void main(String[] args){
        String filename = args[0];
        Parser parser = new Parser(filename);
        CodeWriter codeWriter = new CodeWriter(filename);

        while(parser.hasMoreCommands()){

            String code_line = parser.advance();

            if(code_line == null){
                continue;
            }
            
            String command_type = parser.commandType();

            if (command_type.equals("C_ARITHMETIC")){
                String command = parser.arg1();
                codeWriter.writeArithmetic(code_line, command);
            }
            else {
                String segment = parser.arg1();
                String value = parser.arg2();
                codeWriter.writePushPop(code_line, command_type, segment, value);
            }
        }

        parser.close();
        codeWriter.close();
    }
}

