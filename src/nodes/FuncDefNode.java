package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import java.util.HashMap;
import msc.*;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Function Definition Node
 * Def <id >[ func_def_params ]: <function_return>{ <f_body>}
 */
public class FuncDefNode implements JottTree{

    IDNode funcName;
    FDefParamsNode params;
    TypeNode returnType;
    FuncBodyNode body;

    public FuncDefNode(IDNode name, FDefParamsNode params,
                        TypeNode returnType, FuncBodyNode body){
        this.funcName = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }                    
    
    public static FuncDefNode parse(ArrayList <Token> tokens) throws Exception{
        if(tokens.isEmpty()){
            throw new SyntaxError("Expected Def got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }       
        
        Token lastToken = tokens.get(0);

        if (!(tokens.get(0).getToken().equals("Def") || tokens.get(0).getToken().equals("def"))){
            throw new SyntaxError("Expected Def got " + lastToken.getToken(), lastToken);
        }

        lastToken = tokens.remove(0);

        IDNode name = IDNode.parse(tokens);

        if(tokens.isEmpty() || tokens.get(0).getTokenType() != TokenType.L_BRACKET){
            throw new SyntaxError(
                    "Function definition missing left bracket [ got " + lastToken.getToken(), lastToken);
        }
        lastToken = tokens.remove(0);

        // Check if the next token is R_BRACKET, meaning an empty parameter list
        FDefParamsNode params;
        if (tokens.get(0).getTokenType() == TokenType.R_BRACKET) {
            // No parameters, just an empty ParamsNode
            params = new FDefParamsNode();
            //tokens.remove(0); // Remove R_BRACKET token
        } else {
            // Parse the parameters normally
            params = FDefParamsNode.parse(tokens);
        }

        if(tokens.isEmpty() || tokens.get(0).getTokenType() != TokenType.R_BRACKET){
            throw new SyntaxError( "Function definition missing right bracket ] got " + lastToken.getToken(), lastToken);
        }
        lastToken = tokens.remove(0);

        if(tokens.isEmpty() || tokens.get(0).getTokenType() != TokenType.COLON){
            throw new SyntaxError("Function definition missing colon : got " + lastToken.getToken(), lastToken);        
        }
        lastToken = tokens.remove(0);

        TypeNode returnType = TypeNode.parse(tokens, true);

        if(tokens.isEmpty() || tokens.get(0).getTokenType() != TokenType.L_BRACE){
            throw new SyntaxError(
                    "Function definition missing left brace ] got " + lastToken.getToken(), lastToken);
        }
        lastToken = tokens.remove(0);

        FuncBodyNode body;
        if (tokens.get(0).getTokenType() == TokenType.R_BRACE) {
            body = new FuncBodyNode(new ArrayList<>(), null);
        } else {
            // Parse the body normally
            body = FuncBodyNode.parse(tokens);
        }

        if(tokens.isEmpty() || tokens.get(0).getTokenType() != TokenType.R_BRACE){
            throw new SyntaxError("Expected '}' at end of function definition but found " + lastToken.getToken(), lastToken);
        }
        
        tokens.remove(0);

        return new FuncDefNode(name, params, returnType, body);
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott(){
        
        StringBuilder jottString = new StringBuilder();
        jottString.append("Def ");

        // Function Name
        jottString.append(funcName.convertToJott()).append(" ");

        jottString.append("[");

        // Function Params
        jottString.append(params.convertToJott()).append(" ");

        jottString.append("]:");

        // Return Type
        jottString.append(returnType.convertToJott()).append(" ");

        jottString.append("{");

        // Body
        jottString.append(body.convertToJott()).append(" ");

        jottString.append("}");

        return jottString.toString();
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {

        String _name = this.funcName.getToken().getToken();
        String _returnType = this.returnType.type.getToken();

        //NOTE the parameters will be filled in by the FDefParamsNode
        FunctionInfo info = new FunctionInfo(_name, _returnType, new HashMap<>());

        // Return error if func already defined in symbol table
        if (symbolTable.getFunction(_name) != null) {
            throw new SemanticError("Function " + _name + " already defined in symbol table", this.funcName.getToken());
        }

        // Add function to symbol table
        symbolTable.addFunction(_name, info);

        symbolTable.enterScope(_name);

        if (_name.equals("print" ) || _name.equals("concat" ) || (_name.equals("length" ))) {
            throw new SemanticError("Func name can't be: " + this.funcName.getToken(), this.funcName.getToken());
        }
        if(_name.equals("main")) {
            if(this.params.id != null) {
                throw new SemanticError("Main function should not take any params", this.funcName.getToken());
            }
            if(this.returnType.getType() != DataType.VOID) {
                throw new SemanticError("Main function should have return type VOID", this.funcName.getToken());
            }
        }

        //Valid kids
        this.funcName.validateTree(symbolTable);
        this.params.validateTree(symbolTable);
        this.returnType.validateTree(symbolTable);
        this.body.validateTree(symbolTable);

        symbolTable.exitScope();

        return true;
    }

    /**
     * Enter function scope and execute body
     */
    @Override
    public Object execute(SymbolTable symbolTable) throws Exception {
        // Enter function scope
        String _name = this.funcName.getToken().getToken();
        symbolTable.enterScope(_name);

        // TODO I don't believe we do anything with function params here... 

        // Execute the function body
        return body.execute(symbolTable);
    }

    public static void main(String[] args) {
        System.out.println("Testing FuncDefNode Validation");
    
        SymbolTable symbolTable = new SymbolTable();
    
        // Test Case 1: Valid Function Definition
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Def", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Def
            tokens.add(new Token("main", "testFile.jott", 1, TokenType.ID_KEYWORD)); // main
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens.add(new Token(":", "testFile.jott", 1, TokenType.COLON)); // :
            tokens.add(new Token("Void", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Void
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("String", "testFile.jott", 2, TokenType.ID_KEYWORD)); // String
            tokens.add(new Token("s", "testFile.jott", 2, TokenType.ID_KEYWORD)); // s
            tokens.add(new Token(";", "testFile.jott", 2, TokenType.SEMICOLON)); // ;
            tokens.add(new Token("}", "testFile.jott", 4, TokenType.R_BRACE)); // }
    
            FuncDefNode funcDefNode = FuncDefNode.parse(tokens);
            funcDefNode.validateTree(symbolTable);
            System.out.println("Test Case 1 Passed: Valid function definition.");
        } catch (Exception e) {
            System.err.println("Test Case 1 Failed: " + e.getMessage());
        }
    
        // Test Case 2: Duplicate Function Name
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Def", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Def
            tokens.add(new Token("main", "testFile.jott", 1, TokenType.ID_KEYWORD)); // main
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens.add(new Token(":", "testFile.jott", 1, TokenType.COLON)); // :
            tokens.add(new Token("Void", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Void
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("Integer", "testFile.jott", 2, TokenType.ID_KEYWORD)); // Integer
            tokens.add(new Token("i", "testFile.jott", 2, TokenType.ID_KEYWORD)); // i
            tokens.add(new Token(";", "testFile.jott", 2, TokenType.SEMICOLON)); // ;
            tokens.add(new Token("}", "testFile.jott", 4, TokenType.R_BRACE)); // }
    
            FuncDefNode funcDefNode = FuncDefNode.parse(tokens);
            funcDefNode.validateTree(symbolTable); // Duplicate function
    
            System.err.println("Test Case 2 Failed: Expected duplicate function name error.");
        } catch (Exception e) {
            System.out.println("Test Case 2 Passed: " + e.getMessage());
        }
    
        // Test Case 3: Function with Parameters
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Def", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Def
            tokens.add(new Token("sum", "testFile.jott", 1, TokenType.ID_KEYWORD)); // sum
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Integer
            tokens.add(new Token("a", "testFile.jott", 1, TokenType.ID_KEYWORD)); // a
            tokens.add(new Token(",", "testFile.jott", 1, TokenType.COMMA)); // ,
            tokens.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Integer
            tokens.add(new Token("b", "testFile.jott", 1, TokenType.ID_KEYWORD)); // b
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens.add(new Token(":", "testFile.jott", 1, TokenType.COLON)); // :
            tokens.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Integer
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("return", "testFile.jott", 2, TokenType.ID_KEYWORD)); // return
            tokens.add(new Token("a", "testFile.jott", 2, TokenType.ID_KEYWORD)); // a
            tokens.add(new Token("+", "testFile.jott", 2, TokenType.MATH_OP)); // +
            tokens.add(new Token("b", "testFile.jott", 2, TokenType.ID_KEYWORD)); // b
            tokens.add(new Token(";", "testFile.jott", 2, TokenType.SEMICOLON)); // ;
            tokens.add(new Token("}", "testFile.jott", 3, TokenType.R_BRACE)); // }
    
            FuncDefNode funcDefNode = FuncDefNode.parse(tokens);
            funcDefNode.validateTree(symbolTable);
            System.out.println("Test Case 3 Passed: Function with parameters validated.");
        } catch (Exception e) {
            System.err.println("Test Case 3 Failed: " + e.getMessage());
        }
    }
}