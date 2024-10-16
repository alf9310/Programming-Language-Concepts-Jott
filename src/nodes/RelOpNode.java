package nodes;

import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

/**
 * RelOpNode
 * relational operation like >, <, >=, <=, ==, !=
 */
public class RelOpNode implements OperatorNode{

    Token operator;

    public RelOpNode(Token operator) {
        this.operator = operator;
    }

    // Returns Relational Operator Node if a valid relop
    // Otherwise Throws SyntaxError Exception
    public static RelOpNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("Empty token list for relational operator");        
        }
        Token currentToken = tokens.get(0);
        // Make sure token is type REL_OP
        if(currentToken.getTokenType() != TokenType.REL_OP){
            throw new SyntaxError("TokenType is not REL_OP for relational operator", currentToken);
        }
        // Make sure token is a valid operator character
        if(!(currentToken.getToken().equals(">") || 
        currentToken.getToken().equals(">=") || 
        currentToken.getToken().equals("<") || 
        currentToken.getToken().equals("<=") || 
        currentToken.getToken().equals("==") || 
        currentToken.getToken().equals("!="))){
            throw new SyntaxError("token is not a valid character for relational operator", currentToken);
        }

        Token operator = tokens.remove(0);
        return new RelOpNode(operator);
    }

    @Override
    public String convertToJott() {
        return operator.getToken();
    }

    @Override
    public boolean validateTree() {
        // To be implemented in phase 3
        throw new UnsupportedOperationException("Validation not supported yet.");
    }

    @Override
    public void execute() {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("Testing RelOpNode Main Method");

        // Create test cases
        ArrayList<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token(">", "testfile", 1, TokenType.REL_OP));
        tokens1.add(new Token("dummy", "testfile", 1, TokenType.ID_KEYWORD)); // extra token

        ArrayList<Token> tokens2 = new ArrayList<>();
        tokens2.add(new Token("==", "testfile", 2, TokenType.REL_OP));
        tokens2.add(new Token("dummy", "testfile", 2, TokenType.ID_KEYWORD)); // extra token

        ArrayList<Token> tokens3 = new ArrayList<>();
        tokens3.add(new Token("invalid", "testfile", 3, TokenType.REL_OP));

        try {
            // Test case 1: Valid relational operator ">"
            RelOpNode relOpNode1 = RelOpNode.parse(tokens1);
            System.out.println("Parsed RelOpNode 1: " + relOpNode1.convertToJott());

            // Test case 2: Valid relational operator "=="
            RelOpNode relOpNode2 = RelOpNode.parse(tokens2);
            System.out.println("Parsed RelOpNode 2: " + relOpNode2.convertToJott());

            // Test case 3: Invalid operator
            RelOpNode relOpNode3 = RelOpNode.parse(tokens3); // should throw an exception
            System.out.println("Parsed RelOpNode 3: " + relOpNode3.convertToJott());

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}