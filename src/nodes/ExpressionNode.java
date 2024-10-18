package nodes;

import java.util.ArrayList;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Expression Node
 * <operand> | <operand> <relop> <operand> | 
 * <operand> <mathop> <operand> | <string_literal> |
 * <bool>
 */
public interface ExpressionNode extends JottTree {

    // Determine if the node is BooleanNode, StringLiteralNode, or BinaryOpNode(RelOp & MathOp). 
    // Returns the proper node type that you created above.
    public static ExpressionNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("ExpressionNode Error: Empty token list for expression");        
        }

        // <operand> | <operand> <relop> <operand> | <operand> <mathop> <operand> 
        if (isOperand(tokens.get(0))) {

            if (tokens.size() > 2) {
                // Check if the next token is <relop> or <mathop>
                Token op = tokens.get(1);
                if (op.getTokenType() == TokenType.REL_OP || op.getTokenType() == TokenType.MATH_OP) {
                    return BinaryOpNode.parse(tokens);
                }
            }

            // <operand>
            return OperandNode.parse(tokens);

        // < string_literal >
        } else if(tokens.get(0).getTokenType() == TokenType.STRING){
            return StringLiteralNode.parse(tokens);

        // < bool >
        } else if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD){
            if(tokens.get(0).getToken().equals("True") || tokens.get(0).getToken().equals("False")){
                return BooleanNode.parse(tokens);
            }
        }

        throw new SyntaxError("ExpressionNode Error: Invalid expression token", tokens.get(0));  
    }

    // Helper method to determine if a token is an operand (<id> | <num> | <func_call> | -<num>)
    // Return true if it is
    static boolean isOperand(Token token) {
        TokenType type = token.getTokenType();
        // Check for valid operand types
        if (type == TokenType.ID_KEYWORD) {
            return true;
        } else if (type == TokenType.NUMBER) {
            return true;
        } else if (type == TokenType.FC_HEADER) {
            return true;
        } else if (type == TokenType.MATH_OP && token.getToken().equals("-")) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Testing ExpressionNode Main Method");

        // Test case 1: Simple operand
        testSingleOperand();
        // Test case 2: Simple mathematical operation
        testSimpleMathOperation();
        // Test case 3: Simple relational operation
        testSimpleRelationalOperation();
        // Test case 4: Invalid expression (invalid operator)
        testInvalidOperator();
        // Test case 5: Boolean literal
        testBooleanLiteral();
        // Test case 6: String literal
        testStringLiteral();
    }

    private static void testSingleOperand() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("42", "testFile.jott", 1, TokenType.NUMBER));

            ExpressionNode result = ExpressionNode.parse(tokens);
            System.out.println("Test 1 (Single Operand): " + result.convertToJott());
        } catch (Exception e) {
            System.err.println("Test 1 Failed: " + e.getMessage());
        }
    }

    private static void testSimpleMathOperation() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("5", "testFile.jott", 2, TokenType.NUMBER));
            tokens.add(new Token("+", "testFile.jott", 2, TokenType.MATH_OP));
            tokens.add(new Token("3", "testFile.jott", 2, TokenType.NUMBER));

            ExpressionNode result = ExpressionNode.parse(tokens);
            System.out.println("Test 2 (Simple Math Operation): " + result.convertToJott());
        } catch (Exception e) {
            System.err.println("Test 2 Failed: " + e.getMessage());
        }
    }

    private static void testSimpleRelationalOperation() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("10", "testFile.jott", 3, TokenType.NUMBER));
            tokens.add(new Token(">", "testFile.jott", 3, TokenType.REL_OP));
            tokens.add(new Token("4", "testFile.jott", 3, TokenType.NUMBER));

            ExpressionNode result = ExpressionNode.parse(tokens);
            System.out.println("Test 3 (Simple Relational Operation): " + result.convertToJott());
        } catch (Exception e) {
            System.err.println("Test 3 Failed: " + e.getMessage());
        }
    }

    private static void testInvalidOperator() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("2", "testFile.jott", 4, TokenType.NUMBER));
            tokens.add(new Token("**", "testFile.jott", 4, TokenType.MATH_OP));
            tokens.add(new Token("8", "testFile.jott", 4, TokenType.NUMBER));

            ExpressionNode result = ExpressionNode.parse(tokens);
            System.out.println("Test 4 (Invalid Operator): " + result.convertToJott());
        } catch (Exception e) {
            System.err.println("Test 4 Passed (Expected Error): " + e.getMessage());
        }
    }

    private static void testBooleanLiteral() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("True", "testFile.jott", 5, TokenType.ID_KEYWORD));

            ExpressionNode result = ExpressionNode.parse(tokens);
            System.out.println("Test 5 (Boolean Literal): " + result.convertToJott());
        } catch (Exception e) {
            System.err.println("Test 5 Failed: " + e.getMessage());
        }
    }

    private static void testStringLiteral() {
        try {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("\"Hello, World!\"", "testFile.jott", 6, TokenType.STRING));

            ExpressionNode result = ExpressionNode.parse(tokens);
            System.out.println("Test 6 (String Literal): " + result.convertToJott());
        } catch (Exception e) {
            System.err.println("Test 6 Failed: " + e.getMessage());
        }
    }
}
