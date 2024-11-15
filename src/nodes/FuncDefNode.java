package nodes;

import java.util.ArrayList;
import java.util.HashMap;

import errors.SemanticError;
import errors.SyntaxError;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;
import msc.*;

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

        if (!tokens.get(0).getToken().equals("Def")){
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

        //Valid kids
        this.funcName.validateTree(symbolTable);
        this.params.validateTree(symbolTable);
        this.returnType.validateTree(symbolTable);
        this.body.validateTree(symbolTable);


        return true;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("Testing FuncDefNode Main Method");

        
        try {
            // Sample tokens for function definition:
            // Def main[]:Void { 
            //     ::print[5]; 
            //     ::print["foo bar"]; 
            // /}
            ArrayList<Token> tokens = new ArrayList<>();

            // Add tokens corresponding to the function definition
            tokens.add(new Token("Def", "testFile.jott", 1, TokenType.ID_KEYWORD));  // Def
            tokens.add(new Token("main", "testFile.jott", 1, TokenType.ID_KEYWORD));  // main
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET));  // [
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET));  // ]
            tokens.add(new Token(":", "testFile.jott", 1, TokenType.COLON));  // :
            tokens.add(new Token("Void", "testFile.jott", 1, TokenType.ID_KEYWORD));  // Void
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE));  // {
            tokens.add(new Token("::", "testFile.jott", 2, TokenType.FC_HEADER));  // ::
            tokens.add(new Token("print", "testFile.jott", 2, TokenType.ID_KEYWORD));  // print
            tokens.add(new Token("[", "testFile.jott", 2, TokenType.L_BRACKET));  // [
            tokens.add(new Token("5", "testFile.jott", 2, TokenType.NUMBER));  // 5
            tokens.add(new Token("]", "testFile.jott", 2, TokenType.R_BRACKET));  // ]
            tokens.add(new Token(";", "testFile.jott", 2, TokenType.SEMICOLON));  // ;
            tokens.add(new Token("}", "testFile.jott", 4, TokenType.R_BRACE));  // }

            // Parse the function definition
            FuncDefNode funcDefNode = FuncDefNode.parse(tokens);

            // Convert the parsed function back to Jott code and print
            System.out.println("Parsed 'Def main[]:Void { ::print[5]; }' to  " + funcDefNode.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}