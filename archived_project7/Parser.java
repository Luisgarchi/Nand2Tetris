import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Parser {
    private Scanner scanner;
    private String VMcode;
    private String[] parsedCode;
    private String command;
    private String commandType;
    private String segment;
    private String value;

    public Parser(String input_filename){
        try {
            Scanner scanner = new Scanner(new File(input_filename));
            this.scanner = scanner;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        this.VMcode = null;
        this.segment = null;
        this.value = null;
    }

    public boolean hasMoreCommands(){
        return scanner.hasNextLine();
    }

    public String advance(){
        
        this.VMcode = this.scanner.nextLine();
        String vm_code_stripped = this.VMcode.trim();

        if (
            vm_code_stripped.isEmpty() ||       // empty line in VM code should be ignored
            (vm_code_stripped.charAt(0) == '/' && vm_code_stripped.charAt(1) == '/')    // comments in VM code should be ignored
        ){
            return null;
        }
 
        this.parsedCode = this.VMcode.split("\\s+");

        this.command = this.parsedCode[0];
        if(this.parsedCode.length == 3){
            this.segment = this.parsedCode[1];
            this.value = this.parsedCode[2];
        }
        else {
            this.segment = null;
            this.value = null;
        }

        return this.VMcode;
    }

    public String commandType(){
        if(this.command.equals("push")){
            this.commandType = "C_PUSH";
            return "C_PUSH";
        } 
        else if (this.command.equals("pop")){
            this.commandType = "C_POP";
            return "C_POP";
        }
        else {
            this.commandType = "C_ARITHMETIC";
            return "C_ARITHMETIC";
        }
    }

    public String arg1(){
        if(this.commandType.equals("C_ARITHMETIC")){
            return this.command;
        }
        else if(this.commandType.equals("C_PUSH") || this.commandType.equals("C_POP")){
            return this.segment;
        }
        throw new Error("Invalid command type. Method 'arg1' only supports commands: 'C_ARITHMETIC', 'C_PUSH' and 'C_POP'.");
    }

    public String arg2(){
        if(this.commandType.equals("C_PUSH") || this.commandType.equals("C_POP")){
            return this.value;
        }
        throw new Error("Incorrect use of method 'arg2'. Should only be called when command type is 'C_PUSH' or 'C_POP'.");
    }

    public void close() {
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}
