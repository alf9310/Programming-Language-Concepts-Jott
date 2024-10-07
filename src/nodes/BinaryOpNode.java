package nodes;

import provided.JottTree;
import provided.Token;
import java.util.ArrayList;

public class BinaryOpNode implements JottTree {
    private JottTree leftOperand;
    private Token operator;
    private JottTree rightOperand;

    public BinaryOperationNode(JottTree left, Token op, JottTree right) {
        this.leftOperand = left;
        this.operator = op;
        this.rightOperand = right;
    }

    // Parsing Binary Operations
    public static BinaryOperationNode parse(ArrayList<Token> tokens) throws Exception {
        // Parse the left operand first
        JottTree left = ExpressionNode.parse(tokens);
        if (tokens.isEmpty()) {
            throw new Exception("Unexpected end of input");
        }

        // Parse the operator
        Token op = tokens.remove(0);
        if (!isValidOperator(op)) {
            throw new Exception("Invalid operator: " + op.getToken());
        }

        // Parse the right operand
        JottTree right = ExpressionNode.parse(tokens);

        return new BinaryOperationNode(left, op, right);
    }

    // Checking if the operator is valid
    private static boolean isValidOperator(Token token) {
        String[] validOps = {"+", "-", "*", "/", ">", ">=", "<", "<=", "==", "!="};
        for (String op : validOps) {
            if (op.equals(token.getToken())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String convertToJott() {
        return leftOperand.convertToJott() + " " + operator.getToken() + " " + rightOperand.convertToJott();
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
}
