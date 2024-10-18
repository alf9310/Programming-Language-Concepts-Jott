package nodes;

import java.util.ArrayList;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Type Node and Function Return
 *  Double | Integer | String | Boolean | Void
 */
public class TypeNode implements JottTree{

    Token type;

    public TypeNode(Token type) {
        this.type = type;
    }

    // Overload to default FuncNode to false
    public static TypeNode parse(ArrayList<Token> tokens) throws Exception {
        return parse(tokens, false);
    }
    // Returns Type Node if valid type
    // Otherwise Throws SyntaxError Exception
    public static TypeNode parse(ArrayList<Token> tokens, Boolean FuncNode) throws Exception {

        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("TypeNode Error: Empty token list for type");
        }
        Token currentToken = tokens.get(0);
        // Make sure token is type ID_KEYWORD
        if (currentToken.getTokenType() != TokenType.ID_KEYWORD) {
            throw new SyntaxError("TypeNode Error: TokenType is not ID_KEYWORD", currentToken);
        }
        // Make sure token is valid
        if (FuncNode) {
            if (!(currentToken.getToken().equals("Double")) && !(currentToken.getToken().equals("Integer"))
                    && !(currentToken.getToken().equals("String")) && !(currentToken.getToken().equals("Boolean")) 
                    && !(currentToken.getToken().equals("Void"))) {
                throw new SyntaxError("TypeNode Error: Token is not 'Double' or 'Integer' or 'String' or 'Boolean' or 'Void", currentToken);
            }
            Token type = tokens.remove(0);
            return new TypeNode(type);

        } else {
            if (!(currentToken.getToken().equals("Double")) && !(currentToken.getToken().equals("Integer"))
                    && !(currentToken.getToken().equals("String")) && !(currentToken.getToken().equals("Boolean"))) {
                throw new SyntaxError(String.format("TypeNode Error: Token '%s' is not 'Double' or 'Integer' or 'String' or 'Boolean'", currentToken.getToken()), currentToken);
            }
            Token type = tokens.remove(0);
            return new TypeNode(type);
        }

    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return type.getToken();
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
        System.out.println("Testing TypeNode Main Method");

        // Test Case 0: Valid TypeNode "Void"
        ArrayList<Token> tokens0 = new ArrayList<>();
        tokens0.add(new Token("Void", "test.jott", 1, TokenType.ID_KEYWORD));
        try {
            TypeNode node0 = TypeNode.parse(tokens0, true);
            System.out.println("Parsed TypeNode: " + node0.convertToJott()); // Expected: Void
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Test Case 1: Valid TypeNode "Double"
        ArrayList<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token("Double", "test.jott", 1, TokenType.ID_KEYWORD));
        try {
            TypeNode node1 = TypeNode.parse(tokens1);
            System.out.println("Parsed TypeNode: " + node1.convertToJott()); // Expected: Double
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Test Case 2: Valid TypeNode "Integer"
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