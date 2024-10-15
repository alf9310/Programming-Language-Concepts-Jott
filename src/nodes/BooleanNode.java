package nodes;

import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

/*
 * Boolean Node
 * True | False
 */
public class BooleanNode implements ExpressionNode{

    Token bool;

    public BooleanNode(Token bool) {
        this.bool = bool;
    }

    // Determine if the node is IDNode, number node, or FunctionCall node. Returns
    // the proper node type that you created above.
    public static BooleanNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new Exception("Syntax Error\nEmpty token list for boolean\n"); //TODO, better errors
        }
        if(!(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD)){
            throw new Exception("Syntax Error\nTokenType is not ID_KEYWORD for boolean\n" + tokens.get(0).getFilename() + ":" + tokens.get(0).getLineNum()); //TODO, better errors
        }
        if(!(tokens.get(0).getToken().equals("True")) && !(tokens.get(0).getToken().equals("False"))){
            throw new Exception("Syntax Error\nBoolean is not True or False\n" + tokens.get(0).getFilename() + ":" + tokens.get(0).getLineNum()); //TODO, better errors
        }

        Token bool = tokens.remove(0);
        return new BooleanNode(bool);
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return bool.getToken();
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
        System.out.println("Testing BooleanNode Main Method");

        // Test Case 1: Valid Boolean "True"
        ArrayList<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token("True", "test.jott", 1, TokenType.ID_KEYWORD));
        try {
            BooleanNode node1 = BooleanNode.parse(tokens1);
            System.out.println("Parsed BooleanNode: " + node1.convertToJott());  // Expected: True
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Test Case 2: Valid Boolean "False"
        ArrayList<Token> tokens2 = new ArrayList<>();
        tokens2.add(new Token("False", "test.jott", 2, TokenType.ID_KEYWORD));
        try {
            BooleanNode node2 = BooleanNode.parse(tokens2);
            System.out.println("Parsed BooleanNode: " + node2.convertToJott());  // Expected: False
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Test Case 3: Invalid Boolean (not True/False)
        ArrayList<Token> tokens3 = new ArrayList<>();
        tokens3.add(new Token("Maybe", "test.jott", 3, TokenType.ID_KEYWORD));
        try {
            BooleanNode node3 = BooleanNode.parse(tokens3);
            System.out.println("Parsed BooleanNode: " + node3.convertToJott());  // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage());  // Expected error
        }

        // Test Case 4: Empty Token List
        ArrayList<Token> tokens4 = new ArrayList<>();
        try {
            BooleanNode node4 = BooleanNode.parse(tokens4);
            System.out.println("Parsed BooleanNode: " + node4.convertToJott());  // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage());  // Expected error
        }
    }
}