package nodes;

import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

/*
 * Number Node
 * A number
 */
public class NumberNode implements OperandNode {

    Token number;

    public NumberNode(Token number) {
        this.number = number;
    }

    // Returns Number Node if a number
    // Otherwise Throws SyntaxError Exception
    public static NumberNode parse(ArrayList <Token> tokens) throws Exception{
        // Check if there is tokens
        if(tokens.isEmpty()){
		    throw new SyntaxError("Empty token list for number"); 
        }

        Token currentToken = tokens.get(0);
        // Make sure token is type NUMBER
        if(!(currentToken.getTokenType() == TokenType.NUMBER)){
            throw new SyntaxError("Number type is not NUMBER", currentToken); 
        }

        Token number = tokens.remove(0);
        return new NumberNode(number);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return number.getToken();
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
            // Create a test token list
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("123", "testFile.jott", 1, TokenType.NUMBER));

            // Test the parse method
            NumberNode numberNode = NumberNode.parse(tokens);

            // Output the result
            System.out.println("Parsed Number: " + numberNode.convertToJott());

        } catch (Exception e) {
            // Catch and print any exception
            System.err.println("Error: " + e.getMessage());
        }
    }
}
