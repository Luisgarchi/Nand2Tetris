import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class CodeWriter {

    private String current_file;
    private FileWriter output;
    private String current_instruction;

    // Logical operations label counter
    private int eq_label_count;
    private int gt_label_count;
    private int lt_label_count;

    // Function return label counter
    private Hashtable<String, Integer> return_count_table;

    // Current Function variable for prefixing to labels
    private String current_function;

    public CodeWriter(String programName){

        String output_filename = programName + ".asm";

        try {
            FileWriter fileWriter = new FileWriter(output_filename);
            this.output = fileWriter;
        } catch(IOException e){
            e.printStackTrace(); 
        }

        this.current_file = "";
        this.current_instruction = "";
        this.eq_label_count = 0;
        this.gt_label_count = 0;
        this.lt_label_count = 0;
        this.current_function="";
        this.return_count_table = new Hashtable<>();
    }

    public void setFileName(String filename){
        this.current_file = filename.substring(0, filename.lastIndexOf('.'));
    }


    private void writeCode(String[] code){

        // Takes an sequence of comamnds and writes them line by line to the output file

        try {
            this.output.write("// " + this.current_instruction);
            this.output.write(System.lineSeparator());
            for(int i = 0; i < code.length; i++){
                this.output.write(code[i]);
                this.output.write(System.lineSeparator());
            }
        } catch(IOException e){
            e.printStackTrace(); 
        }
    }

    public void writeInit(){

        String[] code = {
            // SP = 256
            "@256",
            "D=A",
            "@SP",
            "M=D",

            // call Sys.init 

            // push returnAddress
            "@Sys.init$ret.0",
            "D=A",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",

            // push LCL
            "@LCL",
            "D=M",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",
            
            // push ARG
            "@ARG",
            "D=M",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",

            // push THIS
            "@THIS",
            "D=M",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",

            // push THAT
            "@THAT",
            "D=M",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",

            // ARG = SP - 5 - nArgs
            "@5",
            "D=A",
            "@SP",
            "D=M-D",
            "@ARG",
            "M=D",

            // LCL = SP 
            "@SP",
            "D=M",
            "@LCL",
            "M=D",

            // goto functionName
            "@Sys.init",
            "0;JMP",

            // (returnAddress)
            "(Sys.init$ret.0)",
            "@Sys.init$ret.0",
            "0;JMP"
        };

        this.writeCode(code);
    }


    public void writeLabel(String instruction, String label){
        
        this.current_instruction = instruction;

        String format_label = "(" + this.current_function + "$" + label + ")";

        String[] code = {
            format_label
        };

        this.writeCode(code);
    }


    public void writeGoto(String instruction, String label){

        this.current_instruction = instruction;

        String label_address = "@" + this.current_function + "$" + label;

        String[] code = {
            label_address,
            "0;JMP"
        };

        this.writeCode(code);
    }


    public void writeIf(String instruction, String label){

        this.current_instruction = instruction;

        String label_address = "@" + this.current_function + "$" + label;

        String[] code = {

            "@SP",
            "AM=M-1",
            "D=M",
            label_address,
            "D;JNE"
        };

        this.writeCode(code);
    }

    public void writeFunction(String instruction, String function_name, String num_local_var){

        this.current_instruction = instruction;

        // Set the function name so we have the correct labels 
        this.current_function = function_name;

        // 1 = function label, num_local_var = amount of times to push a local variable (0), 4 = code per push
        int code_length = 1 + Integer.parseInt(num_local_var) * 4;

        String[] code = new String[code_length];

        // Add function label
        String function_label = "(" + function_name + ")";
        code[0] = function_label;

        // Push n local variables
        for(int i = 0; i < Integer.parseInt(num_local_var); i++){
            code[4*i + 1] = "@SP";
            code[4*i + 2] = "AM=M+1";
            code[4*i + 3] = "A=A-1";
            code[4*i + 4] = "M=0";
        }

        this.writeCode(code);
    }


    public void writeCall(String instruction, String function_name, String num_args){

        this.current_instruction = instruction;
    
        // Get the return label count for the specified function
        if(!this.return_count_table.containsKey(function_name)){
            this.return_count_table.put(function_name, 0);
        }
        int return_label_count = this.return_count_table.get(function_name);
        String returnAddress = function_name + "$ret." + Integer.toString(return_label_count);

        // Format instructions
        String returnAddress_address = "@" + returnAddress;
        String returnAddress_label = "(" + returnAddress + ")";
        String function_address = "@" + function_name;
        String num_args_address = "@" + num_args;

        String[] code = {

            // push returnAddress
            returnAddress_address,
            "D=A",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",

            // push LCL
            "@LCL",
            "D=M",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",

            // push ARG
            "@ARG",
            "D=M",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",

            // push THIS
            "@THIS",
            "D=M",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",

            // push THAT
            "@THAT",
            "D=M",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",

            // ARG = SP - 5 - nArgs
            "@5",
            "D=A",
            num_args_address,
            "D=D+A",
            "@SP",
            "D=M-D",
            "@ARG",
            "M=D",

            // LCL = SP
            "@SP",
            "D=M",
            "@LCL",
            "M=D",

            // goTo functionName
            function_address,
            "0;JMP",

            // (returnAddress)
            returnAddress_label
        };

        this.return_count_table.put(function_name, 1 + return_label_count);

        this.writeCode(code);
    }


    public void writeReturn(){

        this.current_instruction = "return";
        
        String[] code = {
            //endFrame = LCL

            // returnAddress = *(endFrame - 5)
            "@5",
            "D=A",
            "@LCL",
            "A=M-D",
            "D=M",
            "@R13",
            "M=D",

            // *ARG = pop()
            "@SP",
            "A=M-1",
            "D=M",
            "@ARG",
            "A=M",
            "M=D",

            // SP = ARG + 1
            "@ARG",
            "D=M+1",
            "@SP",
            "M=D",

            // THAT = *(endFrame - 1)
            "@LCL",
            "A=M-1",
            "D=M",
            "@THAT",
            "M=D",

            // THIS = *(endFrame - 2)
            "@2",
            "D=A",
            "@LCL",
            "A=M-D",
            "D=M",
            "@THIS",
            "M=D",

            // ARG = *(endFrame - 3)
            "@3",
            "D=A",
            "@LCL",
            "A=M-D",
            "D=M",
            "@ARG",
            "M=D",

            // LCL = *(endFrame - 4)
            "@4",
            "D=A",
            "@LCL",
            "A=M-D",
            "D=M",
            "@LCL",
            "M=D",

            // goto returnAddress
            "@R13",
            "A=M",
            "0;JMP"
        };

        this.writeCode(code);
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

        this.current_instruction = command;

        // add - arithmetic, 2 args
        if(command.equals("add")){
            this.writeAdd();
        } 

        // sub - arithmetic,  2 args
        else if (command.equals("sub")){
            this.writeSub();
        } 

        // neg - arithmetic, 1 args
        else if (command.equals("neg")){
            this.writeNeg();
        } 

        // eq - logical, 2 args
        else if (command.equals("eq")){
            this.writeEq();
        } 

        // gt - logical, 2 args
        else if (command.equals("gt")){
            this.writeGt();
        } 

        // lt - logical, 2 args
        else if (command.equals("lt")){
            this.writeLt();
        } 

        // and - bitwise, 2 args
        else if (command.equals("and")){
            this.writeAnd();
        } 

        // or - bitwise, 2 args
        else if (command.equals("or")){
            this.writeOr();
        } 

        // not - bitwise, 1 arg
        else if (command.equals("not")){
            this.writeNot();
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
            "@SP",
            "AM=M-1",   // Pop first arg and do SP--
            "D=M",      // Store first arg in D 
            "A=A-1",    // Go to *SP-1 address 
            "M=D+M"     // Get the second arg (M) and ADD to the first arg (D), store result in M.
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
            "@SP",
            "AM=M-1",   // Pop first arg and do SP--
            "D=M",      // Store first arg in D 
            "A=A-1",    // Go to *SP-1 address 
            "M=M-D"     // Get the second arg (M) and SUBTRACT the first arg (D), store result in M.
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
            "@SP",      // Go to top value address
            "A=M",          
            "A=A-1",    // Go to *SP-1 address 
            "M=-M",     // Inplace NEGATION
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
            "@SP",          // Pop first arg
            "AM=M-1",
            "D=M",          // Store first arg in D
            "A=A-1",        // Go to *SP-1 address. After this command M will be equal to the second value   
            "D=M-D",        // SUBTRACT - the first arg value from the second
            "@EQ_TRUE_" + this.eq_label_count,          // Jump to the True condition i.e. arg1 == arg2
            "D;JEQ",        // EQUALS zero check
            "D=0",          // Otherwise set result to 0 (False) and...
            "@EQ_END_" + this.eq_label_count,           // Uncoditionally jump to the end condition
            "0;JMP",
            "(EQ_TRUE_" + this.eq_label_count + ")",    // Set result to 1 (True)
            "D=-1",         // TRUE == -1 (all ones, 0xFFFF)
            "(EQ_END_" + this.eq_label_count + ")",
            "@SP",
            "A=M",
            "A=A-1",
            "M=D",
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
            "@SP",          // Pop first arg
            "AM=M-1",
            "D=M",          // Store first arg in D
            "A=A-1",        // Go to *SP-1 address. After this command M will be equal to the second value   
            "D=M-D",        // SUBTRACT - the first arg value from the second
            "@GT_TRUE_" + this.gt_label_count,          // Jump to the True condition i.e. arg1 == arg2
            "D;JGT",        // GREATER THAN zero check
            "D=0",          // Otherwise set result to 0 (False) and...
            "@GT_END_" + this.gt_label_count,           // Uncoditionally jump to the end condition
            "0;JMP",
            "(GT_TRUE_" + this.gt_label_count + ")",    // Set result to 1 (True)
            "D=-1",         // TRUE == -1 (all ones, 0xFFFF)   
            "(GT_END_" + this.gt_label_count + ")", 
            "@SP",
            "A=M",
            "A=A-1",
            "M=D",
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
            "@SP",          // Pop first arg
            "AM=M-1",
            "D=M",          // Store first arg in D
            "A=A-1",        // Go to *SP-1 address. After this command M will be equal to the second value   
            "D=M-D",        // SUBTRACT - the first arg value from the second
            "@LT_TRUE_" + this.lt_label_count,          // Jump to the True condition i.e. arg1 == arg2
            "D;JLT",        // GREATER THAN zero check
            "D=0",          // Otherwise set result to 0 (False) and...
            "@LT_END_" + this.lt_label_count,           // Uncoditionally jump to the end condition
            "0;JMP",
            "(LT_TRUE_" + this.lt_label_count + ")",    // Set result to 1 (True)
            "D=-1",         // TRUE == -1 (all ones, 0xFFFF)
            "(LT_END_" + this.lt_label_count + ")",
            "@SP",
            "A=M",
            "A=A-1",
            "M=D",
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
            "@SP",      
            "AM=M-1",   // Pop first arg and do SP--
            "D=M",      // Store first arg in D 
            "A=A-1",    // Go to *SP-1 address 
            "M=D&M",    // Get the second arg (M) and perform BITWISE AND the first arg (D), store result in M.
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
            "@SP",
            "AM=M-1",   // Pop first arg and do SP--
            "D=M",      // Store first arg in D 
            "A=A-1",    // Go to *SP-1 address 
            "M=D|M",    // Get the second arg (M) and perform BITWISE OR the first arg (D), store result in M.
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
            "@SP",      // Go to top value address
            "A=M",   
            "A=A-1",
            "M=!M",     // Inplace BITWISE NOT
        };

        this.writeCode(code);
    }


    /*
    public void writePushPop(String instruction, String command, String segment, String index){

        this.current_instruction = instruction;

        if(command.equals("C_POP")){
            this.writePop(segment, index);
        } else if(command.equals("C_PUSH")){
            this.writePush(segment, index);
        }
    }
    */

    public void writePop(String instruction, String segment, String index){
        // SP--
        // addr = segment + i
        // *addr = *SP

        this.current_instruction = instruction;
        
        if(
            segment.equals("local") || 
            segment.equals("argument") || 
            segment.equals("this") || 
            segment.equals("that")
        ){
            this.popBasicSegment(segment, index);
        }
        else if (segment.equals("temp")){
            this.popTempSegment(index);
        }
        else if (segment.equals("static")){
            this.popStaticSegment(index);
        }
        else if (segment.equals("pointer")){
            this.popPointerSegment(index);
        }
        else {
            throw new Error("Invalid Segment: '" + segment + "'. Method 'writePop' only supports the segments: 'local', 'argument', 'this', 'that', 'temp', 'static' and 'pointer'.");
        }

    }


    private void popBasicSegment(String segment, String index){
        
        String seg_addr;
        if(segment.equals("local")){
            seg_addr = "@LCL";
        }
        else if(segment.equals("argument")){
            seg_addr = "@ARG";
        } 
        else if(segment.equals("this")){
            seg_addr = "@THIS";
        }
        else if  (segment.equals("that")){
            seg_addr = "@THAT";
        }
        else {
            throw new Error("Invalid Segment. Method 'popBasicSegment' only supports segments 'local', 'argument', 'this' and 'that'.");
        }

        String index_addr = "@" + index;

        String[] code = {
            // addr = segment + i
            seg_addr,
            "D=M",
            index_addr,
            "D=D+A",
            "@R13",
            "M=D",
            // *addr = *SP--
            "@SP",
            "AM=M-1",
            "D=M",
            "@R13",
            "A=M",
            "M=D"
        };

        this.writeCode(code);
    }

    private void popTempSegment(String index){

        String temp_addr = "@" + String.valueOf(5 + Integer.valueOf(index));
        
        String[] code = {
            // *addr = *SP--
            "@SP",
            "AM=M-1",
            "D=M",
            temp_addr,
            "M=D"
        };

        this.writeCode(code);
    }


    private void popStaticSegment(String index){

        String static_addr = "@" + this.current_file +"." + index;

        String[] code = {
            // *addr = *SP--
            "@SP",
            "AM=M-1",
            "D=M",
            // If it is the first time the parser sees the static segment address 
            // the parser will go ahead and allocate it for us in the next available variable symbol location
            static_addr,      
            "M=D"
        };

        this.writeCode(code);
    }


    private void popPointerSegment(String index){

        String pointer_addr;        

        if(index.equals("0")){
            pointer_addr = "@THIS";
        }
        else if(index.equals("1")){
            pointer_addr = "@THAT";
        }
        else {
            throw new Error("Invalid pointer index. Method 'popPointerSegment' only supports pointer segment index '0' or '1'.");
        }

        String[] code = {
            // *addr = *SP--
            "@SP",
            "AM=M-1",
            "D=M",
            pointer_addr,
            "M=D" 
        };

        this.writeCode(code);
    }

    public void writePush(String instruction, String segment, String index){
        // addr = segment + i
        // *SP = *addr
        // SP++

        this.current_instruction = instruction;

        if(
            segment.equals("local") || 
            segment.equals("argument") || 
            segment.equals("this") || 
            segment.equals("that")
        ){
            this.pushBasicSegmnet(segment, index);
        }
        else if (segment.equals("temp")){
            this.pushTempSegment(index);
        }
        else if(segment.equals("constant")){
            this.pushConstant(index);
        }
        else if(segment.equals("static")){
            this.pushStaticSegment(index);
        }
        else if(segment.equals("pointer")){
            this.pushPointerSegment(index);
        }
        else {
            throw new Error("Invalid Segment. Method 'writePush' only supports the segments: 'local', 'argument', 'this', 'that', 'temp', 'constant', 'static' and 'pointer'.");
        }
    }

    private void pushBasicSegmnet(String segment, String index){
        
        String seg_addr;
        if(segment.equals("local")){
            seg_addr = "@LCL";
        }
        else if(segment.equals("argument")){
            seg_addr = "@ARG";
        } 
        else if(segment.equals("this")){
            seg_addr = "@THIS";
        }
        else if  (segment.equals("that")){
            seg_addr = "@THAT";
        }
        else {
            throw new Error("Invalid Ssegment. Method 'popBasicSegment' only supports segments 'local', 'argument', 'this' and 'that'.");
        }

        String index_addr = "@" + index;

        String[] code = {

            // addr = segment + i
            seg_addr,
            "D=M",
            index_addr,
            "A=D+A",
            "D=M",      // store *addr in D
            // SP++
            // *(SP-1) = *addr
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",
        };

        this.writeCode(code);
    }


    private void pushTempSegment(String index){
        
        String temp_addr = "@" + String.valueOf(5 + Integer.valueOf(index));

        String[] code = {
            temp_addr,
            "D=M",
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D"
        };

        this.writeCode(code);
    }


    private void pushConstant(String index){
        // *SP = index
        // SP++

        String const_addr = "@" + index;

        String[] code = {
            const_addr,
            "D=A",      // Stores the address value
            // SP++
            // *(SP-1) = *addr
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",
        };

        this.writeCode(code);
    }

    
    private void pushStaticSegment(String index){
        // *SP = index
        // SP++

        String static_addr = "@" + this.current_file +"." + index;

        String[] code = {
            // *SP = *addr
            static_addr,
            "D=M",      // Stores contents of the static variable
            // SP++
            // *(SP-1) = *addr
            "@SP",      
            "AM=M+1",
            "A=A-1",
            "M=D",
        };

        this.writeCode(code);
    }

    private void pushPointerSegment(String index){
        // *SP = THIS/THAT
        // SP++

        String pointer_addr;
        
        if(index.equals("0")){
            pointer_addr = "@THIS";
        }
        else if (index.equals("1")){
            pointer_addr = "@THAT";
        }
        else {
            throw new Error("Invalid pointer index. Method 'pushPointerSegment' only supports pointer segment index '0' or '1'.");
        }

        String[] code = {
            pointer_addr,
            "D=M",
            // SP++
            // *(SP-1) = THIS/THAT
            "@SP",
            "AM=M+1",
            "A=A-1",
            "M=D",
        };

        this.writeCode(code);
    }


    public void close(){
        try {
            this.output.close();
        } catch(IOException e){
            e.printStackTrace(); 
        }
    }
}
