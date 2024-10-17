package nodes;

import java.util.ArrayList;
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
    FunctionBodyNode body;

    public FuncDefNode(IDNode name, FDefParamsNode params,
                        TypeNode returnType, FunctionBodyNode body){
        this.funcName = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }                    
    
    public static FuncDefNode parse(ArrayList <Token> tokens) throws Exception{
        if(!tokens.get(0).getToken().equals("Def")){
            throw new SyntaxError("Function definition does not start with the correct keyword");
        }
        tokens.remove(0);

        IDNode name = IDNode.parse(tokens);

        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET){
            throw new SyntaxError("Function definition missing left bracket");
        }
        tokens.remove(0);

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

        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET){
            throw new SyntaxError("Function definition missing right bracket");
        }
        tokens.remove(0);

        if(tokens.get(0).getTokenType() != TokenType.COLON){
            throw new SyntaxError("Function definition missing colon");
        }
        tokens.remove(0);

        TypeNode returnType = TypeNode.parse(tokens, true);

        if(tokens.get(0).getTokenType() != TokenType.L_BRACE){
            throw new SyntaxError("Function definition missing left brace");
        }
        tokens.remove(0);

        //FunctionBodyNode body = FunctionBodyNode.parse(tokens);

        // Check if the next token is R_BRACE, meaning an empty parameter list
        FunctionBodyNode body;
        if (tokens.get(0).getTokenType() == TokenType.R_BRACE) {
            // No parameters, just an empty FunctionBodyNode
            body = new FunctionBodyNode();
        } else {
            // Parse the body normally
            body = FunctionBodyNode.parse(tokens);
        }

        if(tokens.get(0).getTokenType() != TokenType.R_BRACE){
            throw new SyntaxError("Function body missing right brace");
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
    public boolean validateTree() {
        throw new UnsupportedOperationException("Not supported yet.");
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
            tokens.add(new Token("}", "testFile.jott", 4, TokenType.R_BRACE));  // }

            // Parse the function definition
            FuncDefNode funcDefNode = FuncDefNode.parse(tokens);

            // Convert the parsed function back to Jott code and print
            System.out.println("Parsed FuncDefNode: " + funcDefNode.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}