import java.io.FileWriter;
import java.io.IOException;


public class CodeWriter {

    private FileWriter output;
    private int eq_label_count;
    private int gt_label_count;
    private int lt_label_count;

    public CodeWriter(String filename){

        try (FileWriter fileWriter = new FileWriter(filename)){
            this.output = fileWriter;
        } catch(IOException e){
            e.printStackTrace(); 
        }

        this.eq_label_count = 0;
        this.gt_label_count = 0;
        this.lt_label_count = 0;
    }
    
    public void writeArithmetic(String command){

        /* Write any of the following commands: 
            - add   (arithmetic 2 arg)
            - sub   (arithmetic 2 arg)
            - neg   (arithmetic 1 arg)
            - eq    (logical 2 arg)
            - gt    (logical 2 arg)
            - lt    (logical 2 arg)
            - and   (bitwise 2 arg)
            - or    (bitwise 2 arg)
            - not   (bitwise 1 arg)
        */

        // add - arithmetic, 2 args
        if(command == "add"){
            this.writeAdd();
        } 

        // sub - arithmetic,  2 args
        else if (command == "sub") {
            this.writeSub();
        } 

        // neg - arithmetic, 1 args
        else if (command == "neg") {
            this.writeNeg();
        } 

        // eq - logical, 2 args
        else if (command == "eq") {
            this.writeEq();
        } 

        // gt - logical, 2 args
        else if (command == "gt") {
            this.writeGt();
        } 

        // lt - logical, 2 args
        else if (command == "lt") {
            this.writeLt();
        } 

        // and - bitwise, 2 args
        else if (command == "and") {
            this.writeAnd();
        } 

        // or - bitwise, 2 args
        else if (command == "or") {
            this.writeOr();
        } 

        // not - bitwise, 1 arg
        else if (command == "not") {
            this.writeNot();
        } 

    }


    private void writeCode(String[] code){

        // Takes an sequence of comamnds and writes them line by line to the output file

        try {
            for(int i = 0; i < code.length; i++){
                this.output.write(code[i] + System.lineSeparator());
            }
        } catch(IOException e){
            e.printStackTrace(); 
        }
    }
    

    private void writeAdd(){

        /*  Arguments: 2 (top two values on the stack)
            ADDITION Operation: 
            - Pops the top two values, 
            - ADDS them, 
            - and pushes the result back on the stack. 
        */

        String[] code = {
            "@SP",      // Pop top
            "M=M-1", 
            "A=M", 
            "D=M",      // Store top in D
            "@SP",      // Pop bottom
            "M=M-1", 
            "A=M",      // After this command M will be equal to the bottom value
            "M=M+D",    // ADD - we add the top value to the bottom
            "@SP",      // Increment stack pointer
            "M=M+1"
        };

        this.writeCode(code);
    }


    private void writeSub(){

        /*  Arguments: 2 (top two values on the stack)
            SUBTRACTION Operation: 
            - Pops the top two values, 
            - SUBTRACT the top value from the second-to-top value, 
            - and pushes the result back on the stack.
        */

        String[] code = {
            "@SP",      // Pop top
            "M=M-1", 
            "A=M", 
            "D=M",      // Store top in D
            "@SP",      // Pop bottom
            "M=M-1",    
            "A=M",      // After this command M will be equal to the bottom value
            "M=M-D",    // SUBTRACT - we subtract the top value from the bottom
            "@SP",      // Push result onto stack
            "M=M+1"
        };

        this.writeCode(code);
    }


    private void writeNeg(){

        /*  Arguments: 1 (top stack value)
            Negation Operation (x = -x): 
            - Pops the value, 
            - performs negation operation, 
            - and pushes the result back on the stack.
        */

        String[] code = {
            "@SP",      // Pop top
            "M=M-1", 
            "A=M", 
            "M=-M",     // NEGATE
            "@SP",      // Increment stack pointer
            "M=M+1"
        };

        this.writeCode(code);
    }
        

    private void writeEq(){

        /*  Arguments:  2 (top two values on the stack)
            EQUALITY Operation: 
            - Pops the top two values,
            - Subtracts the top value from the bottom
            - Checks if the result EQUALS zero and then applies conditional jumps to push either 0 (false) or 1 (true) to the stack
        */

        String[] code = {
            "@SP",          // Pop top
            "M=M-1", 
            "A=M", 
            "D=M",          // Store top in D
            "@SP",          // Pop bottom
            "M=M-1",    
            "A=M",          // After this command M will be equal to the bottom value
            "D=M-D",        // SUBTRACT - we subtract the top value from the bottom
            "@EQ_TRUE_" + this.eq_label_count,          // Jump to the True condition i.e. top == bottom
            "D; JEQ",       // EQUALS zero check
            "D=0",          // Otherwise set result to 0 (False) and...
            "@EQ_END_" + this.eq_label_count,           // Uncoditionally jump to the end condition
            "0;JMP",
            "(EQ_TRUE_" + this.eq_label_count + ")",    // Set result to 1 (True)
            "D=1",
            "(EQ_END_" + this.eq_label_count + ")",     // Push the result on to the stack
            "@SP",
            "A=M",
            "M=D",
            "@SP",          // Increment stack pointer
            "M=M+1"
        };

        // Increment label counter for Equality operations.
        this.eq_label_count++;

        this.writeCode(code);
    }
      

    private void writeGt(){

        /*  Arguments:  2 (top two values on the stack)
            GREATER THAN Operation: 
            - Pops the top two values,
            - Subtracts the top value from the bottom
            - Checks if the result is GREATER THAN zero and then applies conditional jumps to push either 0 (false) or 1 (true) to the stack
        */

        String[] code = {
            "@SP",          // Pop top
            "M=M-1", 
            "A=M", 
            "D=M",          // Store top in D
            "@SP",          // Pop bottom
            "M=M-1",    
            "A=M",          // After this command M will be equal to the bottom value
            "D=M-D",        // SUBTRACT - we subtract the top value from the bottom
            "@GT_TRUE_" + this.gt_label_count,          // Jump to the True condition i.e. top == bottom
            "D; JGT",       // GREATER THAN check
            "D=0",          // Otherwise set result to 0 (False) and...
            "@GT_END_" + this.gt_label_count,           // Uncoditionally jump to the end condition
            "0;JMP",
            "(GT_TRUE_" + this.gt_label_count + ")",    // Set result to 1 (True)
            "D=1",
            "(GT_END_" + this.gt_label_count + ")",     // Push the result on to the stack
            "@SP",
            "A=M",
            "M=D",
            "@SP",          // Increment stack pointer
            "M=M+1"
        };

        // Increment label counter for Equality operations.
        this.gt_label_count++;

        this.writeCode(code);
    }


    private void writeLt(){

        /*  Arguments:  2 (top two values on the stack)
            LESS THAN Operation: 
            - Pops the top two values,
            - Subtracts the top value from the bottom
            - Checks if the result is LESS THAN zero and then applies conditional jumps to push either 0 (false) or 1 (true) to the stack
        */

        String[] code = {
            "@SP",          // Pop top
            "M=M-1", 
            "A=M", 
            "D=M",          // Store top in D
            "@SP",          // Pop bottom
            "M=M-1",    
            "A=M",          // After this command M will be equal to the bottom value
            "D=M-D",        // SUBTRACT - we subtract the top value from the bottom
            "@LT_TRUE_" + this.lt_label_count,          // Jump to the True condition i.e. top == bottom
            "D; JLT",       // LESS THAN check
            "D=0",          // Otherwise set result to 0 (False) and...
            "@LT_END_" + this.lt_label_count,           // Uncoditionally jump to the end condition
            "0;JMP",
            "(LT_TRUE_" + this.lt_label_count + ")",    // Set result to 1 (True)
            "D=1",
            "(LT_END_" + this.lt_label_count + ")",     // Push the result on to the stack
            "@SP",
            "A=M",
            "M=D",
            "@SP",          // Increment stack pointer
            "M=M+1"
        };

        // Increment label counter for Equality operations.
        this.lt_label_count++;

        this.writeCode(code);
    }


    private void writeAnd(){

        /*  Arguments: 2 (top two values on the stack)
            BITWISE AND Operation: 
            - Pops the top two values, 
            - Performs a BITWISE AND between the top two values, 
            - and pushes the result back.
        */

        String[] code = {
            "@SP",      // Pop top
            "M=M-1", 
            "A=M", 
            "D=M",      // Store top in D
            "@SP",      // Pop top
            "M=M-1", 
            "A=M",      // After this command M will be equal to the bottom value
            "M=D&M",    // BITWISE AND
            "@SP",      // Increment stack pointer
            "M=M+1"
        };

        this.writeCode(code);
    }


    private void writeOr(){

        /*  Arguments: 2 (top two values on the stack)
            BITWISE OR Operation: 
            - Pops the top two values, 
            - Performs a BITWISE OR between the top two values, 
            - and pushes the result back.
        */

        String[] code = {
            "@SP",      // Pop top
            "M=M-1", 
            "A=M", 
            "D=M",      // Store top in D
            "@SP",      // Pop top
            "M=M-1", 
            "A=M",      // After this command M will be equal to the bottom value
            "M=D|M",    // BITWISE OR
            "@SP",      // Increment stack pointer
            "M=M+1"
        };

        this.writeCode(code);
    }


    private void writeNot(){

        /*  Arguments: 1 (top stack value)
            BITWISE NOT Operation: 
            - Pops the value, 
            - Performs a BITWISE NOT, 
            - and pushes the result back.
        */

        String[] code = {
            "@SP",      // Pop top
            "M=M-1", 
            "A=M", 
            "M=!M",     // BITWISE NOT
            "@SP",      // Increment stack pointer
            "M=M+1"
        };

        this.writeCode(code);
    }



    public void writePushPop(String command, String segment, String index){
        if(command == "C_POP"){
            this.writePop(segment, index);
        } else if(command == "C_PUSH"){
            this.writePush(segment, index);
        }
    }

    private boolean isBasicSegment(String segment){
        return (
            segment == "local" || 
            segment == "argument" || 
            segment == "this" || 
            segment == "that"
        );
    }

    private void writeBasicSegment(String segment, String index){
        
        String seg_addr;
        if(segment == "local"){
            seg_addr = "@LCL";
        }
        else if(segment == "argument"){
            seg_addr = "@ARG";
        } 
        else if(segment == "this"){
            seg_addr = "@THIS";
        }
        else if(segment == "that"){
            seg_addr = "@THAT";
        }

        String a_index = "@" + index;

        String[] code = {
            // SP--
            "@SP",
            "M=M-1",
            // addr = segment + i
            seg_addr,
            "D=M",
            a_index,
            "D=D+A",
            "@addr",
            "M=D",
            // *addr = *SP
            "@SP",
            "A=M",
            "D=M",
            "@addr",
            "M=D"
        };
    }

    private void writePop(String segment, String index){
        // SP--
        // addr = segment + i
        // *addr = *SP

        if(this.isBasicSegment(segment)){
            this.writeBasicSegment(segment, segment);
        }

    }




    private void writePush(String segment, String index){
        // addr = segment + i
        // *SP = *addr
        // SP++
    }

    public void close(){

    }
}
