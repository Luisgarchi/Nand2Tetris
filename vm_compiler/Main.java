
public class Main{

    public static void Main(String[] args){
        String filename = args[0];

        Parser parser = new Parser(filename);
        CodeWriter codeWriter = new CodeWriter();

        while(parser.hasMoreCommands()){

        }

    }
}

