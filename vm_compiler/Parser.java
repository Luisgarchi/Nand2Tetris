import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Parser {
    private Scanner scanner;
    private String VMcode;
    private String command;
    private String commandType;
    private String arg1;
    private String arg2;

    public Parser(File file){
        try {
            Scanner scanner = new Scanner(file);
            this.scanner = scanner;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        this.VMcode = null;
        this.arg1 = null;
        this.arg2 = null;
    }

    public boolean hasMoreCommands(){
        return scanner.hasNextLine();
    }

    public String advance(){
        
        // read the line
        String line = this.scanner.nextLine().trim();

        // Return null if comment or empty
        if (
            line.isEmpty() ||       // empty line in VM code should be ignored
            line.startsWith("//")    // comments in VM code should be ignored
        ){
            return null;
        }

        if (line.contains("//")){
            line = line.substring(0, line.lastIndexOf("//"));
        }

        // Parse the commands
        String[] parsedCode = line.split("\\s+");

        this.VMcode = String.join(" ", parsedCode);


        this.command = parsedCode[0];
        this.arg1 = null;
        this.arg2 = null;

        if(parsedCode.length > 1){
            this.arg1 = parsedCode[1];
        }
        if (parsedCode.length > 2){
            this.arg2 = parsedCode[2];
        }
        
        return this.VMcode;
    }

    public String commandType(){

        if(this.command.equals("push")){
            this.commandType = "C_PUSH";
        } 
        else if (this.command.equals("pop")){
            this.commandType = "C_POP";
        }
        else if (this.command.equals("goto")){
            this.commandType = "C_GOTO";
        }
        else if (this.command.equals("if-goto")){
            this.commandType = "C_IF";
        }
        else if (this.command.equals("label")){
            this.commandType = "C_LABEL";
        }
        else if (this.command.equals("function")){
            this.commandType = "C_FUNCTION";
        }
        else if (this.command.equals("call")){
            this.commandType = "C_CALL";
        }
        else if (this.command.equals("return")){
            this.commandType = "C_RETURN";
        }
        else if (
            this.command.equals("add") ||
            this.command.equals("sub") ||
            this.command.equals("neg") ||
            this.command.equals("eq") ||
            this.command.equals("gt") ||
            this.command.equals("lt") ||
            this.command.equals("and") ||
            this.command.equals("or") || 
            this.command.equals("not")
            
        ) {
            this.commandType = "C_ARITHMETIC";
        }
        else {
            throw new Error(String.format("'%s' - Command invalid", this.command));
        }

        return this.commandType;
    }

    public String arg1(){
        return this.arg1;
        /*
        if(this.arg1 != null){
            return this.arg1;
        }
        throw new Error(String.format("'%s' - Invalid arg1 type for command %s. Method 'arg1' only supports commands: 'C_PUSH', 'C_POP', 'C_GOTO', 'C_IF', 'C_LABEL', 'C_FUNCTION' and 'C_CALL'.", this.arg1, this.command));
        */
    }

    public String arg2(){
        return this.arg2;
        /*
        if(this.arg2 != null){
            return this.arg2;
        }
        throw new Error(String.format("'%s' - Invalid arg2 type for command %s. Should only be called when command type is 'C_PUSH', 'C_POP', 'C_FUNCTION' and 'C_CALL'.", this.arg2, this.command));
        */
    }

    public void close() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}
