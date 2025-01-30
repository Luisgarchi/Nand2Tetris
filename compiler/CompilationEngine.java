public class CompilationEngine {
    
    public CompilationEngine(){

        // The next subroutine call must be compileClass
    }

    // Compile Program Structure

    public void compileClass(){

        // 'class' className '{' classVarDec* subroutineDec* '}'
    }

    public void compileClassVarDec(){

        // ('static'|'field') type varName (',' varName)* ';'

        // compiles a i) field variable or a ii) static variable, declaration of one or more variables
    }

    public void compileSubroutineDec(){

        // ('constructor'|'function'|'method') ('void'|'type') subroutineName '(' parameterList ')' subroutineBody

        // compiles a complete constructor, function or method.

    }

    public void compileParameterList(){

        // ((type varName) (',' type varName)*)?

        // compile a possibly empty (?) parameter list. 
    }

    public void compileSubroutineBody(){

        // '{' varDec* statements '}'
    }

    public void compilerVarDec(){

        // 'var' type varName (',' varName)* ';'
    }


    // Compile Statements

    public void compileLet(){

        // let' varName ('[' expression ']' )? '=' expression ';'
    }

    public void compileIf(){

        // 'if' '(' expression ')' '{' statements '}'
    }

    public void compileWhile(){

        // 'while' '(' expression ')' '{' statements '}'
    }

    public void compileDo(){

        // 'do' subroutineCall ';'
        
    }

    public void compileReturn(){

        // 'return' expression? ';'
    }

    // Compile Expressions

    public void compileExpression(){
        
    }

    public void compileTerm(){
        /*
         * Compiles a term. If the current token is an identifier, the routine
         * must distinguish between a variable, an array, or a subroutine call.
         * A single look-ahead token, which may be one of '[', '(', '.' suffices
         * to distinguish between the possibilities. Any other token is invalid.
         */

        // terms:
        //      integerConstant | stringConstant | keywordConstant | varName |
        //      varName '[' expression ']' | subroutineCall | '(' expression ')' | unaryOp term


        // subroutineCall:
        //      subroutineName '(' expressionList ')' |
        //      (className | varName) '.' subroutineName '(' expressionList ')'
    }

    public void compileExpressionList(){

        // (expression (',' expression)* )?
        
    }
}
