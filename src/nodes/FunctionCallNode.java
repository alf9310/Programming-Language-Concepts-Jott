package nodes;

import java.util.ArrayList;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/*
 * Function Call Node
 * ::<id>[<params>]
 */
public class FunctionCallNode implements OperandNode, BodyStmtNode {

    IDNode id;
    ParamsNode params;

    public FunctionCallNode(IDNode id, ParamsNode params){   
	    this.id = id;
        this.params = params;
    }

    public static FunctionCallNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("Expected Function Header :: got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }

        Token currentToken = tokens.get(0);

        // Make sure token is type FC_HEADER ::
        if(!(currentToken.getTokenType() == TokenType.FC_HEADER)){
            throw new SyntaxError("Expected Function Header :: got " + currentToken.getToken(), currentToken);
        }
        tokens.remove(0);

        // Get ID
        IDNode id = IDNode.parse(tokens);

        currentToken = tokens.get(0);
        // Make sure token is type L_BRACKET [
        if(currentToken.getTokenType() != TokenType.L_BRACKET){
            throw new SyntaxError( "Function call missing left bracket [ got " + currentToken.getToken(), currentToken);
        }
        tokens.remove(0);

        // Check if the next token is R_BRACKET, meaning an empty parameter list
        currentToken = tokens.get(0);
        ParamsNode params;
        if (currentToken.getTokenType() == TokenType.R_BRACKET) {
            // No parameters, just an empty ParamsNode
            params = new ParamsNode();
            tokens.remove(0); // Remove R_BRACKET token
        } else {
            // Parse the parameters normally
            params = ParamsNode.parse(tokens);

            // Check if there is tokens
            if(tokens.isEmpty()){
                throw new SyntaxError( "Function call missing right bracket ] got " + JottParser.finalToken.getToken(), JottParser.finalToken);
            }
            // Ensure the next token is an R_BRACKET "]"
            currentToken = tokens.get(0);
            if (currentToken.getTokenType() != TokenType.R_BRACKET) {
                throw new SyntaxError( "Function call missing right bracket ] got " + currentToken.getToken(), currentToken);
            }
            tokens.remove(0); // Remove R_BRACKET token
        }

        // Return a new FunctionCallNode
        return new FunctionCallNode(id, params);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return "::" + id.convertToJott() + "[" + params.convertToJott() + "]"; 
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
        System.out.println("Testing FunctionCallNode Main Method");
        try {
            // Test Case 1: Valid function call with two parameters ::myFunc[param1,param2]
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("::", "testFile.jott", 1, TokenType.FC_HEADER));
            tokens1.add(new Token("myFunc", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET));
            tokens1.add(new Token("param1", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));
            tokens1.add(new Token("param2", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET));
    
            FunctionCallNode funcCallNode1 = FunctionCallNode.parse(tokens1);
            System.out.println("Parsed Function Call 1: " + funcCallNode1.convertToJott());
    
            // Test Case 2: Valid function call with no parameters ::noParamsFunc[]
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("::", "testFile.jott", 2, TokenType.FC_HEADER));
            tokens2.add(new Token("noParamsFunc", "testFile.jott", 2, TokenType.ID_KEYWORD));
            tokens2.add(new Token("[", "testFile.jott", 2, TokenType.L_BRACKET));
            tokens2.add(new Token("]", "testFile.jott", 2, TokenType.R_BRACKET));
    
            FunctionCallNode funcCallNode2 = FunctionCallNode.parse(tokens2);
            System.out.println("Parsed Function Call 2: " + funcCallNode2.convertToJott());
    
            // Test Case 3: Valid function call with three parameters ::multiParamsFunc[param1,param2,param3]
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token("::", "testFile.jott", 3, TokenType.FC_HEADER));
            tokens3.add(new Token("multiParamsFunc", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens3.add(new Token("[", "testFile.jott", 3, TokenType.L_BRACKET));
            tokens3.add(new Token("param1", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens3.add(new Token(",", "testFile.jott", 3, TokenType.COMMA));
            tokens3.add(new Token("param2", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens3.add(new Token(",", "testFile.jott", 3, TokenType.COMMA));
            tokens3.add(new Token("param3", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens3.add(new Token("]", "testFile.jott", 3, TokenType.R_BRACKET));
    
            FunctionCallNode funcCallNode3 = FunctionCallNode.parse(tokens3);
            System.out.println("Parsed Function Call 3: " + funcCallNode3.convertToJott());
    
            // Test Case 4: Invalid function call (missing right bracket) ::invalidFunc[param1,param2
            ArrayList<Token> tokens4 = new ArrayList<>();
            tokens4.add(new Token("::", "testFile.jott", 4, TokenType.FC_HEADER));
            tokens4.add(new Token("invalidFunc", "testFile.jott", 4, TokenType.ID_KEYWORD));
            tokens4.add(new Token("[", "testFile.jott", 4, TokenType.L_BRACKET));
            tokens4.add(new Token("param1", "testFile.jott", 4, TokenType.ID_KEYWORD));
            tokens4.add(new Token(",", "testFile.jott", 4, TokenType.COMMA));
            tokens4.add(new Token("param2", "testFile.jott", 4, TokenType.ID_KEYWORD));
            // Missing R_BRACKET token here
    
            try {
                FunctionCallNode funcCallNode4 = FunctionCallNode.parse(tokens4);
                System.out.println("Parsed Function Call 4: " + funcCallNode4.convertToJott());
            } catch (Exception e) {
                System.err.println("Test Case 4 Failed: " + e.getMessage());
            }
    
            // Test Case 5: Invalid function call (missing left bracket) ::invalidFuncParam
            ArrayList<Token> tokens5 = new ArrayList<>();
            tokens5.add(new Token("::", "testFile.jott", 5, TokenType.FC_HEADER));
            tokens5.add(new Token("invalidFuncParam", "testFile.jott", 5, TokenType.ID_KEYWORD));
            // Missing L_BRACKET token here
            tokens5.add(new Token("param1", "testFile.jott", 5, TokenType.ID_KEYWORD));
            tokens5.add(new Token(",", "testFile.jott", 5, TokenType.COMMA));
            tokens5.add(new Token("param2", "testFile.jott", 5, TokenType.ID_KEYWORD));
            tokens5.add(new Token("]", "testFile.jott", 5, TokenType.R_BRACKET));
    
            try {
                FunctionCallNode funcCallNode5 = FunctionCallNode.parse(tokens5);
                System.out.println("Parsed Function Call 5: " + funcCallNode5.convertToJott());
            } catch (Exception e) {
                System.err.println("Test Case 5 Failed: " + e.getMessage());
            }
    
            // Test Case 6: Valid function call with one parameter ::singleParamFunc[param1]
            ArrayList<Token> tokens6 = new ArrayList<>();
            tokens6.add(new Token("::", "testFile.jott", 6, TokenType.FC_HEADER));
            tokens6.add(new Token("singleParamFunc", "testFile.jott", 6, TokenType.ID_KEYWORD));
            tokens6.add(new Token("[", "testFile.jott", 6, TokenType.L_BRACKET));
            tokens6.add(new Token("param1", "testFile.jott", 6, TokenType.ID_KEYWORD));
            tokens6.add(new Token("]", "testFile.jott", 6, TokenType.R_BRACKET));
    
            FunctionCallNode funcCallNode6 = FunctionCallNode.parse(tokens6);
            System.out.println("Parsed Function Call 6: " + funcCallNode6.convertToJott());
    
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}