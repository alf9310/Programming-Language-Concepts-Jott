package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/*
 * Number Node
 * A number, with negative functionality
 */
public class NumberNode implements OperandNode {

    Token number;
    boolean negative;

    public NumberNode(Token number) {
        this.number = number;
        this.negative = false; // Default to positive
    }

    // Constructor with explicit positive/negative control
    public NumberNode(Token number, boolean negative) {
        this.number = number;
        this.negative = negative;
    }

    // Returns Number Node if a number, supports negatives
    // Otherwise Throws SyntaxError Exception
    public static NumberNode parse(ArrayList<Token> tokens) throws Exception {
        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected Number got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }
        boolean isNegative = false;

        // Check if the first token is a negative sign (-)
        Token currentToken = tokens.get(0);
        if (currentToken.getTokenType() == TokenType.MATH_OP && currentToken.getToken().equals("-")) {
            isNegative = true;
            tokens.remove(0);
        }

        // Make sure the next token is a NUMBER
        currentToken = tokens.get(0);
        if (!(currentToken.getTokenType() == TokenType.NUMBER)) {
            throw new SyntaxError("Expected Number got " + currentToken.getToken(), currentToken);
        }

        Token number = tokens.remove(0);
        return new NumberNode(number, isNegative);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        if (negative) {
            return "-" + number.getToken();
        }
        return number.getToken();
    }

    @Override
    public DataType getType(SymbolTable symbolTable) {
        if (number.getToken().contains(".")) {
            return DataType.DOUBLE;
        }
        return DataType.INTEGER;
    }

    @Override
    public Token getToken() {
        return number;
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) {
        return true;
    }

    @Override
    public Object execute(SymbolTable symbolTable) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Main method to test NumberNode parsing and negation
    public static void main(String[] args) {
        System.out.println("Testing Number Node Main Method");
        try {
            // Test with a positive number
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("123", "testFile.jott", 1, TokenType.NUMBER));
            NumberNode numberNode = NumberNode.parse(tokens);
            System.out.println("Parsed Number (positive): " + numberNode.convertToJott());

            // Test with a negative number
            tokens.clear();
            tokens.add(new Token("-", "testFile.jott", 1, TokenType.MATH_OP));
            tokens.add(new Token("456", "testFile.jott", 1, TokenType.NUMBER));
            NumberNode negativeNumberNode = NumberNode.parse(tokens);
            System.out.println("Parsed Number (negative): " + negativeNumberNode.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}