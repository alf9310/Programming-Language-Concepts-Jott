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
    OperatorNode leftOperand;
    OperatorNode rightOperand;

    public RelOpNode(Token operator, OperatorNode leftOperand, OperatorNode rightOperand) {
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    // Returns Relational Operator Node if a valid relop
    // Otherwise Throws SyntaxError Exception
    public static RelOpNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("RelOpNode Error: Empty token list for relational operator");        
        }
        Token currentToken = tokens.get(0);
        // Make sure token is type REL_OP
        if(currentToken.getTokenType() != TokenType.REL_OP){
            throw new SyntaxError("RelopNode Error: TokenType is not REL_OP for relational operator", currentToken);
        }
        // Make sure token is a valid operator character
        if(!(currentToken.getToken().equals(">") || 
        currentToken.getToken().equals(">=") || 
        currentToken.getToken().equals("<") || 
        currentToken.getToken().equals("<=") || 
        currentToken.getToken().equals("==") || 
        currentToken.getToken().equals("!="))){
            throw new SyntaxError("RelopNode Error: token is not a valid character for relational operator", currentToken);
        }

        Token operator = tokens.remove(0);
        OperatorNode leftOperand = parseOperand(tokens);  // Parse left operand
        OperatorNode rightOperand = parseOperand(tokens); // Parse right operand

        return new RelOpNode(operator, leftOperand, rightOperand);
    }

    /**
    * Helper method to parse an operand from the token list.
    * This method creates an appropriate node based on the token type.
    */
    private static OperatorNode parseOperand(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            throw new SyntaxError("Operand Error: Missing operand after operator.");
        }

        Token operandToken = tokens.remove(0);

        switch (operandToken.getTokenType()) {
            case NUMBER:
                return (OperatorNode) new NumberNode(operandToken); // Should work if NumberNode implements OperatorNode
            case ID_KEYWORD:
                return new IDNode(operandToken); // Should work if IdNode implements OperatorNode
            default:
                throw new SyntaxError("Operand Error: Unexpected token type for operand", operandToken);
        }
    }

    @Override
    public String convertToJott() {
        return operator.getToken();
    }

    @Override
    public boolean validateTree() {
        // Ensure that both operands are not null
        if (leftOperand == null || rightOperand == null) {
            throw new UnsupportedOperationException("RelOpNode Error: Both operands must be present.");
        }
    
        // Ensure operands are of the same type
        if (!leftOperand.getClass().equals(rightOperand.getClass())) {
            throw new UnsupportedOperationException("RelOpNode Error: Operands must be of the same type.");
        }
    
        // Ensure both operands are numeric types (example check, actual implementation may vary)
        if (!(leftOperand instanceof NumberNode) || !(rightOperand instanceof NumberNode)) {
            throw new UnsupportedOperationException("RelOpNode Error: Operands must be numeric.");
        }
    
        return true; // The tree is valid if all checks pass
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