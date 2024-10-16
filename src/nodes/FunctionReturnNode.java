package nodes;

import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * FuncReturn Node
 *  Double | Integer | String | Boolean
 * I think this should return strings. confirm with prof
 */
public class FunctionReturnNode implements JottTree{
    Token func_return;
    public FunctionReturnNode(Token func_return) {
        this.func_return = func_return;
    }

    // Returns func_return Node if valid func_return
    // Otherwise Throws SyntaxError Exception
    public static FunctionReturnNode parse(ArrayList<Token> tokens) throws Exception {

        // Check if there are tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Empty token list for func_return");
        }

        Token currentToken = tokens.get(0);

        // <type>
        if ((currentToken.getToken().equals("Double")) && !(currentToken.getToken().equals("Integer"))
                && !(currentToken.getToken().equals("String")) && !(currentToken.getToken().equals("Boolean"))) {
            return TypeNode.parse(tokens);

            //  Void 
        } else if (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            if (tokens.get(0).getToken().equals("True")) {
                Token type = tokens.remove(0);
                return new FunctionReturnNode(type);            }
        }

        throw new SyntaxError("Invalid expression token for function return node", tokens.get(0));

    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    public String convertToJott() {
        return func_return.getToken();
    }

    public boolean validateTree() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("Testing FunctionReturnNode Main Method");

        // // Test Case 1: Valid Boolean "Double"
        // ArrayList<Token> tokens1 = new ArrayList<>();
        // tokens1.add(new Token("Double", "test.jott", 1, TokenType.ID_KEYWORD));
        // try {
        //     FunctionReturnNode node1 = FunctionReturnNode.parse(tokens1);
        //     System.out.println("Parsed FunctionReturnNode: " + node1.convertToJott()); // Expected: Double
        // } catch (Exception e) {
        //     System.err.println(e.getMessage());
        // }

        // // Test Case 2: Valid Boolean "Integer"
        // ArrayList<Token> tokens2 = new ArrayList<>();
        // tokens2.add(new Token("Integer", "test.jott", 1, TokenType.ID_KEYWORD));
        // try {
        //     FunctionReturnNode node2 = FunctionReturnNode.parse(tokens2);
        //     System.out.println("Parsed FunctionReturnNode: " + node2.convertToJott()); // Expected: Integer
        // } catch (Exception e) {
        //     System.err.println(e.getMessage());
        // }

        // // Test Case 3: Invalid func_return
        // ArrayList<Token> tokens3 = new ArrayList<>();
        // tokens3.add(new Token("Bignum", "test.jott", 3, TokenType.ID_KEYWORD));
        // try {
        //     FunctionReturnNode node3 = FunctionReturnNode.parse(tokens3);
        //     System.out.println("Parsed FunctionReturnNode: " + node3.convertToJott()); // Should throw an error
        // } catch (Exception e) {
        //     System.err.println(e.getMessage()); // Expected error
        // }

        // Test Case 4: Empty Token List
        ArrayList<Token> tokens4 = new ArrayList<>();
        try {
            FunctionReturnNode node4 = FunctionReturnNode.parse(tokens4);
            System.out.println("Parsed FunctionReturnNode: " + node4.convertToJott()); // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage()); // Expected error
        }
    }
}