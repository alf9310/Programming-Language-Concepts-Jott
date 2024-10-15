package nodes;

import provided.Token;
import provided.TokenType;
import java.util.ArrayList;

/*
 * Function Call Node
 * ::<id>[<params>]
 */
public class FunctionCallNode implements OperandNode {

    IDNode id;
    ArrayList<ParamNode> params;

    public FunctionCallNode(IDNode id, ArrayList <ParamNode> params){   
	    this.id = id;
        this.params = params;
    }

    public static FunctionCallNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
		    throw new SyntaxError("Empty token list for function call"); 
        }

        Token currentToken = tokens.get(0);

        // Make sure token is type FC_HEADER (Function Header, ::)
        if(!(currentToken.getTokenType() == TokenType.FC_HEADER)){
            throw new SyntaxError("Function Call does not start with a FC_HEADER", currentToken); 
        }
        tokens.remove(0);

        // Get ID
        IDNode id = IDNode.parse(tokens);

        currentToken = tokens.get(0);

        // Make sure token is type L_BRACKET [
        if(!(currentToken.getTokenType() == TokenType.L_BRACKET)){
            throw new SyntaxError("Function Call does not contain L_BRACKET after id", currentToken); 
        }
        tokens.remove(0);

        // Get Params
        ArrayList<ParamNode> params = ParamNode.parse(tokens);

        currentToken = tokens.get(0);

        // Make sure token is type R_BRACKET ]
        if(!(currentToken.getTokenType() == TokenType.R_BRACKET)){
            throw new SyntaxError("Function Call does not end with R_BRACKET", currentToken); 
        }
        tokens.remove(0);
	    

        return new FunctionCallNode(id, params);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();
        // Function id
        jottString.append(id.convertToJott()).append(" ");

        // Function Params
        for (FuncDefParam param : params) {
            jottString.append(param.convertToJott()).append(" ");
        }

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
        try {
            // Create tokens for a sample function call: ::myFunc[param1,param2]
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("::", "testFile.jott", 1, TokenType.FC_HEADER));
            tokens.add(new Token("myFunc", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET));
            tokens.add(new Token("param1", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));
            tokens.add(new Token("param2", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET));

            // Test parsing the tokens
            FunctionCallNode funcCallNode = FunctionCallNode.parse(tokens);

            // Output the result
            System.out.println("Parsed Function Call: " + funcCallNode.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}
