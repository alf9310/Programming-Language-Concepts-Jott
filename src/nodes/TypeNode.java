package nodes;

import java.util.ArrayList;
import provided.Token;
import provided.TokenType;
import provided.JottTree;

/*
 * Type Node
 *  Double | Integer | String | Boolean
 */
public class TypeNode implements JottTree{

    Token type;

    public TypeNode(Token type) {
        this.type = type;
    }

    // Returns Type Node if valid type
    // Otherwise Throws SyntaxError Exception
    public static TypeNode parse(ArrayList<Token> tokens) throws Exception {

        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Empty token list for type");
        }
        Token currentToken = tokens.get(0);
        // Make sure token is type ID_KEYWORD
        if (currentToken.getTokenType() != TokenType.ID_KEYWORD) {
            throw new SyntaxError("TokenType is not ID_KEYWORD", currentToken);
        }
        // Make sure token is valid
        if (!(currentToken.getToken().equals("Double")) && !(currentToken.getToken().equals("Integer"))
                && !(currentToken.getToken().equals("String")) && !(currentToken.getToken().equals("Boolean"))) {
            throw new SyntaxError("Token is not 'Double' or 'Integer' or 'String' or 'Boolean'", currentToken);
        }

        Token type = tokens.remove(0);
        return new TypeNode(type);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    public String convertToJott() {
        return type.getToken();
    }

    public boolean validateTree() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("Testing TypeNode Main Method");

        // Test Case 1: Valid Boolean "Double"
        ArrayList<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token("Double", "test.jott", 1, TokenType.ID_KEYWORD));
        try {
            TypeNode node1 = TypeNode.parse(tokens1);
            System.out.println("Parsed TypeNode: " + node1.convertToJott()); // Expected: Double
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Test Case 2: Valid Boolean "Integer"
        ArrayList<Token> tokens2 = new ArrayList<>();
        tokens2.add(new Token("Integer", "test.jott", 1, TokenType.ID_KEYWORD));
        try {
            TypeNode node2 = TypeNode.parse(tokens2);
            System.out.println("Parsed TypeNode: " + node2.convertToJott()); // Expected: Integer
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Test Case 3: Invalid type 
        ArrayList<Token> tokens3 = new ArrayList<>();
        tokens3.add(new Token("Bignum", "test.jott", 3, TokenType.ID_KEYWORD));
        try {
            TypeNode node3 = TypeNode.parse(tokens3);
            System.out.println("Parsed TypeNode: " + node3.convertToJott()); // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage()); // Expected error
        }

        // Test Case 4: Empty Token List
        ArrayList<Token> tokens4 = new ArrayList<>();
        try {
            TypeNode node4 = TypeNode.parse(tokens4);
            System.out.println("Parsed TypeNode: " + node4.convertToJott()); // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage()); // Expected error
        }
    }
}