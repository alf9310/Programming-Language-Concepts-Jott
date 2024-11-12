package nodes;

import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

/**
 * MathOpNode
 * Math operation like +, -, *, or /
 */
public class MathOpNode implements OperatorNode{

    Token operator;
    OperatorNode leftOperand;
    OperatorNode rightOperand;

    public MathOpNode(Token operator, OperatorNode leftOperand, OperatorNode rightOperand) {
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    // Returns Math Operator Node if a valid Mathop
    // Otherwise Throws SyntaxError Exception
    public static MathOpNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("MathOpNode Error: Empty token list for Math operator");        
        }
        Token currentToken = tokens.get(0);
        // Make sure token is type Math_OP
        if(currentToken.getTokenType() != TokenType.MATH_OP){
            throw new SyntaxError("MathOpNode Error: TokenType is not MATH_OP for Math operator", currentToken);
        }
        // Make sure token is a valid operator character
        if(!(currentToken.getToken().equals("+") || 
        currentToken.getToken().equals("-") || 
        currentToken.getToken().equals("*") || 
        currentToken.getToken().equals("/"))){
            throw new SyntaxError("MathOpNode Error: token is not a valid character for Math operator", currentToken);
        }

        Token operator = tokens.remove(0);
        OperatorNode leftOperand = parseOperand(tokens);
        OperatorNode rightOperand = parseOperand(tokens);

        return new MathOpNode(operator, leftOperand, rightOperand);
    }

    private static OperatorNode parseOperand(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            throw new SyntaxError("Operand Error: Missing operand after operator.");
        }

        Token operandToken = tokens.remove(0);
        switch (operandToken.getTokenType()) {
            case NUMBER:
                return (OperatorNode) new NumberNode(operandToken);
            case ID_KEYWORD:
                return new IDNode(operandToken);
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
            throw new UnsupportedOperationException("MathOpNode Error: Both operands must be present.");
        }

        // Ensure operands are numeric types
        if (!(leftOperand instanceof NumberNode) || !(rightOperand instanceof NumberNode)) {
            throw new UnsupportedOperationException("MathOpNode Error: Operands must be numeric.");
        }

        // Confirm that the math operator token is valid
        String op = operator.getToken();
        if (!(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/"))) {
            throw new UnsupportedOperationException("MathOpNode Error: Invalid math operator.");
        }

        // Check for division by zero if the operator is "/"
        if (op.equals("/") && ((NumberNode) rightOperand).getToken().getToken().equals("0")) {
            throw new UnsupportedOperationException("MathOpNode Error: Division by zero.");
        }

        return true;
    }

    @Override
    public void execute() {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("Testing MathOpNode Main Method");

        // Create test cases
        ArrayList<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token("*", "testfile", 1, TokenType.MATH_OP));
        tokens1.add(new Token("dummy", "testfile", 1, TokenType.ID_KEYWORD)); // extra token

        ArrayList<Token> tokens2 = new ArrayList<>();
        tokens2.add(new Token("-", "testfile", 2, TokenType.MATH_OP));
        tokens2.add(new Token("dummy", "testfile", 2, TokenType.ID_KEYWORD)); // extra token

        ArrayList<Token> tokens3 = new ArrayList<>();
        tokens3.add(new Token("invalid", "testfile", 3, TokenType.MATH_OP));

        try {
            // Test case 1: Valid Mathational operator ">"
            MathOpNode MathOpNode1 = MathOpNode.parse(tokens1);
            System.out.println("Parsed MathOpNode 1: " + MathOpNode1.convertToJott());

            // Test case 2: Valid Mathational operator "=="
            MathOpNode MathOpNode2 = MathOpNode.parse(tokens2);
            System.out.println("Parsed MathOpNode 2: " + MathOpNode2.convertToJott());

            // Test case 3: Invalid operator
            MathOpNode MathOpNode3 = MathOpNode.parse(tokens3); // should throw an exception
            System.out.println("Parsed MathOpNode 3: " + MathOpNode3.convertToJott());

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}