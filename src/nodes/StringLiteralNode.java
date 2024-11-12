package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import msc.DataType;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/*
 * String Literal Node
 * A string
 */
public class StringLiteralNode implements ExpressionNode{

    Token string;

    public StringLiteralNode(Token string) {
        this.string = string;
    }

    // Returns String Literal Node if a string
    // Otherwise Throws SyntaxError Exception
    public static StringLiteralNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("Expected string literal got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }
        Token currentToken = tokens.get(0);
        // Make sure token is type STRING
        if(currentToken.getTokenType() != TokenType.STRING){
            throw new SyntaxError("Expected string literal got " + currentToken.getToken(), currentToken);
        }

        Token string = tokens.remove(0);
        return new StringLiteralNode(string);
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return string.getToken();
    }

    @Override
    public DataType getType() {
        return DataType.STRING;
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("Testing StringLiteralNode Main Method");

        // Test Case 1: Valid String Literal
        ArrayList<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token("\"Hello, World!\"", "test.jott", 1, TokenType.STRING));
        try {
            StringLiteralNode node1 = StringLiteralNode.parse(tokens1);
            System.out.println("Parsed StringLiteralNode: " + node1.convertToJott());  // Expected: "Hello, World!"
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Test Case 2: Invalid Token Type (not STRING)
        ArrayList<Token> tokens2 = new ArrayList<>();
        tokens2.add(new Token("123", "test.jott", 2, TokenType.NUMBER));
        try {
            StringLiteralNode node2 = StringLiteralNode.parse(tokens2);
            System.out.println("Parsed StringLiteralNode: " + node2.convertToJott());  // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage());  // Expected error: TokenType is not STRING for string literal
        }

        // Test Case 3: Empty Token List
        ArrayList<Token> tokens3 = new ArrayList<>();
        try {
            StringLiteralNode node3 = StringLiteralNode.parse(tokens3);
            System.out.println("Parsed StringLiteralNode: " + node3.convertToJott());  // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage());  // Expected error: Empty token list for string literal
        }

        // Test Case 4: Valid String with escaped characters
        ArrayList<Token> tokens4 = new ArrayList<>();
        tokens4.add(new Token("\"This is a \\\"test\\\" string\"", "test.jott", 3, TokenType.STRING));
        try {
            StringLiteralNode node4 = StringLiteralNode.parse(tokens4);
            System.out.println("Parsed StringLiteralNode: " + node4.convertToJott());  // Expected: "This is a \"test\" string"
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}