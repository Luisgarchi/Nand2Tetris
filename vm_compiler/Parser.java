import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


import java.util.Hashtable;

public class Parser {
    private Scanner scanner;
    private String VMcode;
    private String[] parsedCode;
    private String command;
    private String commandType;
    private String segment;
    private String value;

    public Parser(String filename){
        try (Scanner scanner = new Scanner(new File(filename))){
            this.scanner = scanner;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        this.VMcode = null;
    }

    public boolean hasMoreCommands(){
        return scanner.hasNextLine();
    }

    public void advance(){
        this.VMcode = this.scanner.nextLine();
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
    }

    public String commandType(){
        if(this.command == "push"){
            this.commandType = "C_PUSH";
            return "C_PUSH";
        } 
        else if (this.command == "pop"){
            this.commandType = "C_POP";
            return "C_POP";
        }
        else {
            this.commandType = "C_ARITHMETIC";
            return "C_ARITHMETIC";
        }
    }

    public String arg1(){
        if(this.commandType == "C_ARITHMETIC"){
            return this.command;
        }
        else if(this.commandType == "C_PUSH" || this.commandType == "C_POP"){
            return this.segment;
        }
        return null;
    }

    public String arg2(){
        if(this.commandType == "C_PUSH" || this.commandType == "C_POP"){
            return this.value;
        }
        return null;
    }

}
