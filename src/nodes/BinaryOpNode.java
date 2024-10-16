package nodes;

import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

/*
 * Binary Operation Node
 * Can either be a boolean equation or a mathmatical equation
 * < operand > < relop > < operand > | < operand > < mathop > < operand >
 */
public class BinaryOpNode implements ExpressionNode {

    OperandNode leftOperand;
    OperatorNode operator; // Generic, can be either a RelOpNode or a MathOpNode
    OperandNode rightOperand;

    public BinaryOpNode(OperandNode left, OperatorNode op, OperandNode right) {
        this.leftOperand = left;
        this.operator = op;
        this.rightOperand = right;
    }

    // Parsing Binary Operations
    public static BinaryOpNode parse(ArrayList<Token> tokens) throws Exception {
        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("Empty token list for binary operation");        
        }

        // Parse the left operand first
        OperandNode left = OperandNode.parse(tokens);

        // Parse the operator (can be either MathOpNode or RelOpNode)
        Token op = tokens.get(0); // Don't want to remove, as Math & Rel Op nodes does that
        OperatorNode operator;
        if(op.getTokenType() == TokenType.MATH_OP){
            operator = MathOpNode.parse(tokens);
        } else if(op.getTokenType() == TokenType.REL_OP){
            operator = RelOpNode.parse(tokens);
        } else {
            throw new SyntaxError("Invalid operator", op);
        }

        // Parse the right operand
        OperandNode right = OperandNode.parse(tokens);

        return new BinaryOpNode(left, operator, right);
    }

    @Override
    public String convertToJott() {
        return leftOperand.convertToJott() + " " + operator.convertToJott() + " " + rightOperand.convertToJott();
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
        try {

            System.out.println("Testing BinaryOpNode Main Method");

            // Test Case 1: "5 + 3"
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("5", "testFile.jott", 1, TokenType.NUMBER));
            tokens1.add(new Token("+", "testFile.jott", 1, TokenType.MATH_OP));
            tokens1.add(new Token("3", "testFile.jott", 1, TokenType.NUMBER));

            BinaryOpNode binaryOpNode1 = BinaryOpNode.parse(tokens1);
            System.out.println("Parsed BinaryOpNode (5 + 3): " + binaryOpNode1.convertToJott());

            // Test Case 2: "10 - 4"
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("10", "testFile.jott", 2, TokenType.NUMBER));
            tokens2.add(new Token(">", "testFile.jott", 2, TokenType.REL_OP));
            tokens2.add(new Token("4", "testFile.jott", 2, TokenType.NUMBER));

            BinaryOpNode binaryOpNode2 = BinaryOpNode.parse(tokens2);
            System.out.println("Parsed BinaryOpNode (10 > 4): " + binaryOpNode2.convertToJott());

            // Test Case 3: "2 ** 8", invalid operator
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token("2", "testFile.jott", 3, TokenType.NUMBER));
            tokens3.add(new Token("**", "testFile.jott", 3, TokenType.MATH_OP));
            tokens3.add(new Token("8", "testFile.jott", 3, TokenType.NUMBER));

            BinaryOpNode binaryOpNode3 = BinaryOpNode.parse(tokens3);
            System.out.println("Parsed BinaryOpNode (2 ** 8): " + binaryOpNode3.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}