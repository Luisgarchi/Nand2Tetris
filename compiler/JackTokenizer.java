import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.Set;

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

    private ArrayList<String> tokens;

    private BufferedReader br;
    private BufferedWriter bw;
        
    public JackTokenizer(File file){

        String filename = file.getName();
        
        try{
            // Reader
            BufferedReader br = new BufferedReader(new FileReader(file));
            this.br = br;

            //Writer
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename + ".xml"));
            this.bw = bw;

        } catch(IOException e){
            e.printStackTrace();
        }

        this.tokens = new ArrayList<String>();
        this.tokenize();
    }


    private void tokenize(){

        boolean isMLC = false;          // Multi-Line Comment
        int charIndex = 0;
        String line = "";
        StringBuilder tempToken = new StringBuilder();

        try {
            // Iterate over all the lines of the file
            while ((line = this.br.readLine()) != null){
                
                // Iterate over all the characters of the line
                while (charIndex < line.length()){

                    char currentChar = line.charAt(charIndex);



                    charIndex++;
                }
                charIndex = 0;
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    // QUESTIONABLE

    private void getLine(){

        while(isMultiLineComment){
            this.readLine();
        }


        this.readLine();
        this.cleanLine();

        while(
            (this.currentLine != null) & /* fhf */ this.currentLine != null &
            (this.currentLine.length() == 0)
        ){

            this.readLine();
            this.cleanLine();
        }

        if(this.currentLine == null){
            throw new Error("Cannot initialize a tokenizer for an empty file");
        }

        return;
    }

    private void readLine(){

        try {
            this.currentLine = this.br.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        return;
    }

    private void cleanLine(){

        // Important to remove normal comment before multi-comment data to 
        // avoid setting isMultiLineComment to true when we encounter the following comment
        // "// SOME COMMENT PLUS /* IN THE COMMENT"

        int commentStartIndex;

        // check for and remove multi-comment from a single line
        if(
            this.currentLine.contains("/*") & 
            this.currentLine.contains("*/") & 
            (this.currentLine.indexOf("/*") < this.currentLine.indexOf("*/") - 1)
        ){
            this.currentLine = this.currentLine.replaceAll("\\{\\*.*?\\*\\}", " ");
        }


        if(this.currentLine.contains("  v ")){super
            commentStartIndex = this.currentLine.indexOf("/*");
            this.currentLine = this.currentLine.substring(commentStartIndex, 0);
            // Set isMultiLineComment attribute to true
            this.isMultiLineComment = false;
        }

        // remove comment from line
        if(this.currentLine.contains("//")){
            commentStartIndex = this.currentLine.indexOf("//");
            this.currentLine = this.currentLine.substring(0, commentStartIndex);
        }

        // remove multi-comment from line
        if(this.currentLine.contains("/*")){
            commentStartIndex = this.currentLine.indexOf("/*");
            this.currentLine = this.currentLine.substring(0, commentStartIndex);
            // Set isMultiLineComment attribute to true
            this.isMultiLineComment = true;
        }

        this.currentLine = this.currentLine
            .trim()                             // Remove leading and trailing spaces
            .replaceAll("\\s+", " ");           // Replaces multiple spaces with one
        
        return;
    }

    private void getNextChar(){
        // check if more characters still in the current line
        if(this.charIndex < this.currentLine.length() - 1){
            this.charIndex++;
        }
        else {
            this.getLine();
            this.charIndex = 0;
        }

        this.currentChar = this.currentLine.charAt(this.charIndex);
        return;
    }


    public boolean hasMoreTokens(){
        
        // Check to see if there are more tokens in the file by:
        // 1) Iterating until we get a character that is not a space and
        // 2) checking the file is not finished
        while(
            this.currentLine != null &
            this.currentChar == ' '
        ){
            this.getNextChar();
        }

        // Check that the file has not finished
        return this.currentLine != null;
    }


    public void advance(){

        // Set the current token to the current character
        currentToken.setLength(0);
        currentToken.append(this.currentChar);

        // Reset the current token
        this.tokenType = null;

        // Look at the current char and ask, is the current char an individual token?
        if(checkSymbol()){
            this.tokenType = "symbol";
            this.getNextChar();
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

        return;
    }

    private void writeToken(){

        try{
            this.bw.write("<" + this.tokenType.toLowerCase() + "> ");
            this.bw.write(this.currentToken.toString());
            this.bw.write(" </" + this.tokenType.toLowerCase() + ">\n");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public void Main(){
        try{
            this.bw.write(" <tokens>\n");

            while(this.hasMoreTokens()){
                this.advance();
                this.writeToken();
            }

            this.bw.write(" </tokens>\n");
        }
        catch(IOException e){
            e.printStackTrace();
        }
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


class LineTokenizer{

    /*
        This method "tokenizes" tokens in a single line according to the definition
        of expressions and their respective "regular expressions". The computation
        of each token (or regular express) is achieved by defining their respective 
        finite automatons and handling there individual termination criteria. 
        
        The termination criteria defines the end of a token. Once found the token 
        is saved in a data strucutre. Common termination criteria include:
        - Whitepace
        - An invalid character of the current expression
        - The end of the line being tokenized.

        Characters that define the end of a token for one expression BUT represent
        a state transition in another expression need to be handle explicitly by 
        "looking ahead". This is important in order to handle comments correctly. 
        
        Consider the following two cases when the first character of a token is:
        - The "/" character is a symbol token unless it is followed by another "/"
           in which case it is a single line comment "//". Single like comments are
           not tokenized and discarded.
        - The "*" character is a symbol token unless it is followed by a "/" AND
          the current state of the tokenizing process IS a multi-line comment.
        - The need to know whether the tokenizer is parsing a multi-line comment 
           or not requires employing a state variable implemented as "this.isMLC"
    */


    private static final Set<String> KEYWORDS = Set.of(
        "class" , "constructor" , "function" , "method" , "field" , "static" , "var" , 
        "int" , "char" , "boolean" , "void" , "true" , "false" , "null" , "this" , 
        "let" , "do" , "if" , "else" , "while" , "return" 
    );

    private static final Set<Character> SYMBOLS = Set.of(
        '{', '}', '(', ')', '[', ']',
        '.', ',', ';', '+', '-', '*',
        '/', '&', '<', '>', '=', '~'
    );

    private String line;
    private boolean isMLC;          // Multi-Line Comment
    private int charIndex;
    private StringBuilder tempToken;
    private ArrayList<String> tokens;


    public LineTokenizer(){
        this.tempToken = new StringBuilder();
        this.tokens = new ArrayList<String>();
        this.isMLC = false;
    }

    private void initializeLine(String line){
        this.charIndex = 0;
        this.line = line;
        this.tempToken.setLength(0);
        this.tokens.clear();
    }

    private void addToken(){

        // Add parsed token to tokens
        this.tokens.add(this.tempToken.toString());

        // Reset the temporary token field
        this.tempToken.setLength(0);
    }

    private char getChar(){
        return this.line.charAt(this.charIndex);
    }

    private char getChar(int offset){
        return this.line.charAt(this.charIndex + offset);
    }

    public String[] tokenize(String line){

        /*
            Method responsible for returning parsed tokens of a single line of Jack
            code. The method attempts to find the start of a new token by discarding
            whitespaces. 
            
            When the first character of a potential token is encountered, it is checked 
            against the first state transition of all the potential finite automatons 
            of valid tokens. 
            
            Special characters ("/" & "*") that require look ahead to determine the
            token, are explicitly checked and subsequently handled in their respective 
            method. Because special characters can belong to more than one type of token
            it is necessary to organise the code to check special characters before the 
            the group that they may belong to.

            The method for processesing each token proceeds to parse the current token 
            according to its respective automata. Each method uses the same private fields 
            to the current method (tokenize) to ensure consistency between nested operations. 
        */

        this.initializeLine(line);

        // Iterate over all the characters of the line
        while (this.charIndex < line.length()){

            char currentChar = this.getChar();

            // Check for whitespacing and move on if detected
            boolean isWhiteSpace = (currentChar == ' ') && (this.tempToken.toString() == " ");
            
            if(isWhiteSpace){
                this.charIndex++;
                continue;
            }

            // handle current multiline comment
            if(this.isMLC){
                this.handleMLC();
            }
            // handle forward slash -> tokenizes a symbol or handles either a single or multi-line comment.
            else if(currentChar == '/'){
                this.handleSlash();
            }

            // tokize a string constant
            else if(currentChar == '"'){
                this.tokenizeString();
            }

            // tokenize an integer constant
            else if("0123456789".indexOf(currentChar) != -1){
                this.tokenizeInteger();
            }

            // Check if it is a symbol
            else if(LineTokenizer.SYMBOLS.contains(currentChar)){
                this.tokenizeSymbol();
            }

            // Check for identifier or keyword
            else if(Character.isLetter(currentChar) || currentChar == '_'){
                this.handleIdentifierOrSymbol();
            }
        }
        return (String[]) this.tokens.toArray();
    }


    private void tokenizeString(){

        /*
         * We must append the leading double apostrophe before the loop to avoid it
         * from terminating immedately.
        */

        this.tempToken.append('"');
        this.charIndex++;

        // Keep iterating until we find the end of the string. i.e. the next double apostrophe
        while(
            (this.charIndex < this.line.length()) && 
            (this.getChar() != '"')
        ) {
            this.tempToken.append(this.getChar());    // append the current character
            this.charIndex++;
        }

        // Check that the end of the string was found
        if (this.charIndex >= this.line.length()){
            throw new IllegalStateException("The end of the string was not found in: '" + this.line + "'");
        }

        // Append the TRAILING double apostrophe to the temporary token
        this.tempToken.append('"');

        // Add token and increment the current character index
        this.addToken();
        this.charIndex++;
    }


    private void tokenizeInteger(){
        /*
         * Note: To test wheteher a character is a digit we check if the current character
         *       is in the string "0123456789" using the charAt method. The code leveraegs
         *       the fact that if the character is not in enumerate digits string it will
         *       return -1
        */

        // Keep iterating until we find the end of the integer. i.g. the next char which is not a digit
        while(
            (this.charIndex < line.length()) && 
            ("0123456789".indexOf(this.getChar()) != -1)      
        ) {
            this.tempToken.append(this.getChar());            // append current digit
            this.charIndex++;
        }
        this.addToken();
    }


    private void handleSlash(){
        /*
         * Since a "/" indicates either:
         *      1) A symbol 
         *      2) The start of a SINGLE line comment e.g. // Single line comment here
         *      3) The start of a MULTI-line comment  e.g. /* Multi line comment here
         * it is necessary to look 1 character ahead to determing the presents of either a "/" or a "*"
        */

        // Beforehand, check to see if there is another symbol in the line.
        char charAhead;
        if(this.charIndex + 1 >= this.line.length()){
            this.tempToken.append('/');
            this.addToken();
            this.charIndex++;
            return;
        }
        else {
            this.charIndex++;
            charAhead = this.getChar();
        }

        // handle single line comment
        if(charAhead == '/'){
            // Discard the remainder of the line
            while(this.charIndex < this.line.length()){
                this.charIndex++;
            }
        }
        // handle multi-line comment
        else if(charAhead == '*'){
            // set multi-line comment attribute to true
            this.charIndex++;
            this.isMLC = true;
        }
        // otherwise the slash is just a regular symbol
        else{
            this.tempToken.append('/');
            this.addToken();
            this.charIndex++;
        }
    }

    private void handleMLC(){

        /*
         * Given the tokenizer is inside a multi-line comment, the method aims to find the end
         * of the multi-line comment by finding the following sequence of characters:
         *      1) A '*',
         *      2) followed by a '/''
        */
        
        while(
            (this.charIndex + 1 < this.line.length()) &&    // ensure offset
            !(                                              // not "*/"
                (this.getChar() == '*') && 
                (this.getChar(1) == '/')
            )
        ){
            this.charIndex++;
        }

        // Found end of multi line comment character
        if((this.getChar() == '*') && (this.getChar(1) == '/')){
            this.isMLC = false;
            this.charIndex += 2;    // 2 increments one for tailing '/' and to point index to the next character
        }
        // Otheriwse the end of the line
        else {
            this.charIndex++;
        }
    }

    private void tokenizeSymbol(){
        this.tempToken.append(this.getChar());
        this.addToken();
        this.charIndex++;
    }

    private void handleIdentifierOrSymbol(){

        while(
            (this.charIndex < this.line.length()) && 
            (this.isLetterDigitOrUnderscore(this.getChar()))
        ){
            this.tempToken.append(this.getChar());
            this.charIndex++;
        }
        this.addToken();
    }

    private boolean isLetterDigitOrUnderscore(char character){
        return(
            Character.isLetter(character) ||
            Character.isDigit(character) ||
            character == '_'
        );
    }
}