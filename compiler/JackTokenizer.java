import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;

public class JackTokenizer {

    /*
     * Ignores all comments and white space in the input stream and serializes it into
     * Jack-language tokens. The token types are specfied according to the jack grammar.
    */


    /*
     * keyword: class | constructor | function | method | field | static | var | 
     *          int | char | boolean | void | true | false | null | this | 
     *          let | do | if | else | while | return 
     *
     * symbol:  {   }   (   )   [   ]    
     *          .   ,   ;   +   -   *
     *          /   &   <   >   =   ~ 
     * 
     * integerConstant: a decimal number in the range 0...32767
     * 
     * StringConstant: ' "a sequence of Unicode characters not including double quotes or newlines" '
     * 
     * identifier:      a sequence of letters, digits, and underscore ('_') not starting with a digit
    */

    private String[] keywords = {
        "class" , "constructor" , "function" , "method" , "field" , "static" , "var" , 
        "int" , "char" , "boolean" , "void" , "true" , "false" , "null" , "this" , 
        "let" , "do" , "if" , "else" , "while" , "return" 
    };

    private char[] symbols = {  
        '{', '}', '(', ')', '[', ']',
        '.', ',', ';', '+', '-', '*',
        '/', '&', '<', '>', '=', '~'
    };

    private BufferedReader br;
    private BufferedWriter bw;
    private char currentChar;
    private StringBuilder currentToken;
    private String tokenType;
        
    public JackTokenizer(File file){

        String filename = file.getName();
        
        try{
            // Init Buffered Reader
            BufferedReader br = new BufferedReader(new FileReader(file));
            this.br = br;

            // Init Buffered Writer
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename + ".xml"));
            this.bw = bw;
        } catch(IOException e){
            e.printStackTrace();
        }

        this.currentChar = '\0';

    }

    public void run(){
        
        try {
            while(hasMoreTokens()){
                this.bw.write("<tokens>\n");
                this.writeToken();
                this.bw.write("</tokens>\n");
            }
        } 
        catch (Error e){
            e.printStackTrace();
        }
    }

    public boolean hasMoreTokens(){
        
        // Read the next character from the input file if the current char is not the null character
        if(this.currentChar != '\0'){
            readNextChar();
        }

        // Check that the file has not finished
        return (this.currentChar != -1);
    }

    private void readNextChar(){
        this.currentChar = (char) this.br.read();
        while(this.currentChar == ' '){
            this.currentChar = (char) this.br.read();
        }
        return;
    }

    public void advance(){

        // Read the next character from the input file if the current char is not the null character
        if(this.currentChar != '\0'){
            readNextChar();
        }

        // Set the current token to the current character
        currentToken.setLength(0);
        currentToken.append(currentChar);

        // Reset the current token
        this.tokenType = null;

        // Look at the current char and ask, is the current char an individual token?
        if(checkSymbol()){
            this.tokenType = "symbol";
        }
        else if(checkString()){
            this.tokenType = "stringConstant";
        }
        else if(checkInteger()){
            this.tokenType = "integerConstant";
        }
        else if(checkIdentifier()){

            // because keywords are a subset of identifiers we check for a keyword
            if(checkKeyword()){
                this.tokenType = "keyword";
            }

            else {
                this.tokenType = "identifier";
            }
        }

        else {
            throw new Error("Inavlid token" + this.currentToken);
        }


        this.currentChar = (char) this.br.read();


        return;
    }

    private void writeToken(){
        this.bw.write("<" + this.tokenType.toLowerCase() + "> ");
        this.bw.write(this.currentToken.toString());
        this.bw.write(" </" + this.tokenType.toLowerCase() + ">\n");
    }

    private boolean checkSymbol(){

        // Iterate over symbol list and check if current char is a match
        for(int i = 0; i < this.symbols.length; i++){
            if(this.currentChar == this.symbols[i]){

                // Reset the current char to null
                this.currentChar = '\0';
                return true;
            }
        }
        return false;
    }

    private boolean checkString(){

        // first check to see if the initial character is a double apostrophe '"'
        if(this.currentChar != '"'){
            return false;
        }

        // Reset the current token because we do not want to keep the leading and trailing double apostrophes
        currentToken.setLength(0);

        // keep processing the string body until we reach the next '"'
        while(this.currentChar != '"'){

            if(this.currentChar != '\n'){
                throw new Error("String constants must not contain newline characters");
            }
            readNextChar();
            this.currentToken.append(this.currentChar);
        }

        // RESET the current char to null
        this.currentChar = '\0';
        
        return true;
    }

    private boolean checkInteger(){
        
        if("0123456789".indexOf(this.currentChar) == -1){
            return false;
        }
        
        // keep processing the string body until we reach the next '"'
        while("0123456789".indexOf(this.currentChar) != -1){
            readNextChar();
            this.currentToken.append(this.currentChar);
        }
        // DO NOT set the currentChar to null
        return true;
    }

    private boolean isIdenfierCurrentChar(){
        return (Character.isAlphabetic(this.currentChar) | Character.isDigit(this.currentChar) | this.currentChar == '_');
    }

    private boolean checkIdentifier(){

        //a sequence of letters, digits, and underscore ('_') not starting with a digit
        if(!isIdenfierCurrentChar()){
            return false;
        }

        // keep processing the identifier until we reach a character that is not permitted
        while(isIdenfierCurrentChar()){
            readNextChar();
            this.currentToken.append(this.currentChar);
        }

        // DO NOT set the currentChar to null
        return true;
    }

    private boolean checkKeyword(){
        // Iterate over symbol list and check if current char is a match
        for(int i = 0; i < this.keywords.length; i++){
            if(this.currentToken.equals(this.keywords[i])){
                return true;
            }
        }

        return false;
    }


    public String tokenType(){

        /*
        * returns the type of the current token as a constant:
        * KEYWORD, SYMBOL, IDENTIFIER, INT_CONST, STRING_CONST
        */

        return this.tokenType();
    }

    public String keyWord(){
        return "";
    }

    public char symbol(){
        return 'a';
    }

    public String identifier(){
        return "";
    }

    public int intVal(){
        return 0;
    }

    public String stringVal(){
        return "";
    }

}