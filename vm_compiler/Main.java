import java.io.File;
import java.io.FilenameFilter;

public class Main{

    public static void main(String[] args){

        String input = args[0];

        //String input = "BasicLoop.vm";

        // Check if the input is a single VM file or a directory
        String inputType = (input.endsWith(".vm")) ? "FILE" : "DIRECTORY";

        // Set the output programName name. If input is a single VM file, strip '.vm'. 
        String programName = (inputType.equals("FILE")) ? input.substring(0, input.lastIndexOf('.')) : input;
 
        
        // List files to compile
        File[] files;
        if(inputType.equals("FILE")){
            files = new File[] {
                new File(input)
            };
        }
        else {
            File f = new File(input);
            files = f.listFiles(new VMFileFilter());
        }
        
        // Compile the program
        CodeWriter codeWriter = new CodeWriter(programName);

        if(inputType.equals("DIRECTORY")){
            codeWriter.writeInit();
        }

        for(int i = 0; i < files.length; i++){

            Parser parser = new Parser(files[i]);
            codeWriter.setFileName(files[i].getName());

            while(parser.hasMoreCommands()){

                String code_line = parser.advance();

                if(code_line == null){
                    continue;
                }

                String command_type = parser.commandType();

                if(command_type.equals("C_ARITHMETIC")){
                    codeWriter.writeArithmetic(code_line);
                }
                else if(command_type.equals("C_PUSH")){
                    codeWriter.writePush(code_line, parser.arg1(), parser.arg2());
                }
                else if(command_type.equals("C_POP")){
                    codeWriter.writePop(code_line, parser.arg1(), parser.arg2());
                }
                else if(command_type.equals("C_LABEL")){
                    codeWriter.writeLabel(code_line, parser.arg1());
                }
                else if(command_type.equals("C_GOTO")){
                    codeWriter.writeGoto(code_line, parser.arg1());
                }
                else if(command_type.equals("C_IF")){
                    codeWriter.writeIf(code_line, parser.arg1());
                }
                else if(command_type.equals("C_FUNCTION")){
                    codeWriter.writeFunction(code_line, parser.arg1(), parser.arg2());
                }
                else if(command_type.equals("C_CALL")){
                    codeWriter.writeCall(code_line, parser.arg1(), parser.arg2());
                }
                else if(command_type.equals("C_RETURN")){
                    codeWriter.writeReturn();
                }
            }
            parser.close();
        }
        codeWriter.close();
    }
}

class VMFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return name.endsWith(".vm");
    }
}