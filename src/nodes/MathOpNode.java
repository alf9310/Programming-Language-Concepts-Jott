package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/**
 * MathOpNode
 * Math operation like +, -, *, or /
 */
public class MathOpNode implements OperatorNode {

    Token operator;

    public MathOpNode(Token operator) {
        this.operator = operator;
    }

    // Returns Math Operator Node if a valid Mathop
    // Otherwise Throws SyntaxError Exception
    public static MathOpNode parse(ArrayList<Token> tokens) throws Exception {

        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected Math Operator got " + JottParser.finalToken.getToken(),
                    JottParser.finalToken);
        }
        Token currentToken = tokens.get(0);
        // Make sure token is type Math_OP
        if (currentToken.getTokenType() != TokenType.MATH_OP) {
            throw new SyntaxError("Expected Math Operator got " + currentToken.getToken(), currentToken);
        }
        // Make sure token is a valid operator character
        if (!(currentToken.getToken().equals("+") ||
                currentToken.getToken().equals("-") ||
                currentToken.getToken().equals("*") ||
                currentToken.getToken().equals("/"))) {
            throw new SyntaxError("Expected Math Operator +, - * or / got " + currentToken.getToken(), currentToken);
        }

        Token operator = tokens.remove(0);
        return new MathOpNode(operator);
    }

    @Override
    public String convertToJott() {
        return operator.getToken();
    }

    @Override
    public TokenType getTokenType() {
        return operator.getTokenType();
    }

    @Override
    public Token getToken() {
        return operator;
    }

    @Override
    public DataType getType(SymbolTable symbolTable) {
        return DataType.INTEGER;
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) {
        return true;
    }

    @Override
    public void execute(SymbolTable symbolTable) {
        // Retrieve the two operands from the symbol table
        VarInfo leftOperand = symbolTable.getVar("leftOperand");
        VarInfo rightOperand = symbolTable.getVar("rightOperand");

        if (leftOperand == null || rightOperand == null) {
            throw new RuntimeException("Operands not found in SymbolTable.");
        }

        // Perform the operation based on the type
        String result;
        if (leftOperand.type == DataType.INTEGER) {
            int leftValue = Integer.parseInt(leftOperand.value);
            int rightValue = Integer.parseInt(rightOperand.value);

            switch (operator.getToken()) {
                case "+":
                    result = String.valueOf(leftValue + rightValue);
                    break;
                case "-":
                    result = String.valueOf(leftValue - rightValue);
                    break;
                case "*":
                    result = String.valueOf(leftValue * rightValue);
                    break;
                case "/":
                    if (rightValue == 0) throw new RuntimeException("Division by zero.");
                    result = String.valueOf(leftValue / rightValue);
                    break;
                default:
                    throw new RuntimeException("Unsupported math operator: " + operator.getToken());
            }
        } else if (leftOperand.type == DataType.DOUBLE) {
            double leftValue = Double.parseDouble(leftOperand.value);
            double rightValue = Double.parseDouble(rightOperand.value);

            switch (operator.getToken()) {
                case "+":
                    result = String.valueOf(leftValue + rightValue);
                    break;
                case "-":
                    result = String.valueOf(leftValue - rightValue);
                    break;
                case "*":
                    result = String.valueOf(leftValue * rightValue);
                    break;
                case "/":
                    if (rightValue == 0.0) throw new RuntimeException("Division by zero.");
                    result = String.valueOf(leftValue / rightValue);
                    break;
                default:
                    throw new RuntimeException("Unsupported math operator: " + operator.getToken());
            }
        } else {
            throw new RuntimeException("Unsupported data type for math operation: " + leftOperand.type);
        }

        // Store the result back in the left operand
        leftOperand.value = result;
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