package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
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
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected " + TokenType.MATH_OP + " or " + TokenType.REL_OP + " got "
                    + JottParser.finalToken.getToken(), JottParser.finalToken);
        }

        // Parse the left operand first
        OperandNode left = OperandNode.parse(tokens);

        // Parse the operator (can be either MathOpNode or RelOpNode)
        Token op = tokens.get(0); // Don't want to remove, as Math & Rel Op nodes does that
        OperatorNode operator;
        if (null == op.getTokenType()) {
            throw new SyntaxError(
                    "Expected " + TokenType.MATH_OP + " or " + TokenType.REL_OP + " got " + op.getTokenType(), op);
        } else
            switch (op.getTokenType()) {
                case MATH_OP -> operator = MathOpNode.parse(tokens);
                case REL_OP -> operator = RelOpNode.parse(tokens);
                default -> throw new SyntaxError(
                        "Expected " + TokenType.MATH_OP + " or " + TokenType.REL_OP + " got " + op.getTokenType(), op);
            }

        // Parse the right operand
        OperandNode right = OperandNode.parse(tokens);

        // Check for chaining of math operations (e.g., 3 + 2 * 3)
        if (!tokens.isEmpty() && tokens.get(0).getTokenType() == TokenType.MATH_OP) {
            throw new SyntaxError("Chained math operations are not allowed.", tokens.get(0));
        }

        return new BinaryOpNode(left, operator, right);
    }

    @Override
    public String convertToJott() {
        return leftOperand.convertToJott() + " " + operator.convertToJott() + " " + rightOperand.convertToJott();
    }

    @Override
    public DataType getType(SymbolTable symbolTable) {
        if (operator.getTokenType() == TokenType.REL_OP) {
            // For Relational Operator, returns a Boolean
            return DataType.BOOLEAN;
        }
        // For Math Operator, returns either a INTEGER or DOUBLE
        DataType leftType = leftOperand.getType(symbolTable);
        return leftType;
    }

    @Override
    public Token getToken() {
        return this.leftOperand.getToken();
    }

    /*
     * Operands must be valid
     * Operator must be valid
     * For RelOp:
     * Operands on either side of the operator must be of the same data type
     * For Math Op:
     * Operands on either side of the operator must be either both Integers or both
     * Doubles
     * Division by 0 is not allowed
     */
    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        leftOperand.validateTree(symbolTable);
        operator.validateTree(symbolTable);
        rightOperand.validateTree(symbolTable);

        if (operator.getTokenType() == TokenType.REL_OP) {
            // For Relational Operator checks
            if (leftOperand.getType(symbolTable) != rightOperand.getType(symbolTable)) {
                throw new SemanticError("Operands on either side of the operator must be of the same data type",
                        leftOperand.getToken());
            }
        } else if (operator.getTokenType() == TokenType.MATH_OP) {
            // For Mathematical Operator checks
            DataType leftType = leftOperand.getType(symbolTable);
            DataType rightType = rightOperand.getType(symbolTable);

            if ((leftType == DataType.INTEGER && rightType != DataType.INTEGER) ||
                    (leftType == DataType.DOUBLE && rightType != DataType.DOUBLE)) {
                throw new SemanticError(
                        "Operands on either side of the operator must be either both INTEGERS or DOUBLES",
                        leftOperand.getToken());
            }

            if (operator.convertToJott().equals("/") &&
                    (rightOperand.convertToJott().equals("0") || rightOperand.convertToJott().equals("0.0"))) {
                throw new SemanticError("Division by 0 error", leftOperand.getToken());
            }
        }
        return true;
    }

    @Override
    public void execute() {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }

    public static void main(String[] args) {
        test_validate();
    }

    private static void test_validate() {
        try {
            System.out.println("-----Testing BinaryOpNode Main Method-----");

            // Test Case 1: Valid expression "5 + 3"
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("5", "testFile.jott", 1, TokenType.NUMBER));
            tokens1.add(new Token("+", "testFile.jott", 1, TokenType.MATH_OP));
            tokens1.add(new Token("3", "testFile.jott", 1, TokenType.NUMBER));

            BinaryOpNode binaryOpNode1 = BinaryOpNode.parse(tokens1);
            System.out.println("Parsing BinaryOpNode (5 + 3): " + binaryOpNode1.convertToJott());
            SymbolTable symbolTable = new SymbolTable();
            binaryOpNode1.validateTree(symbolTable); // Should pass without error
            System.out.println("Validation passed for (5 + 3)");

            // Test Case 2: Relational operator with mismatched operand types "5 > id"
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("5", "testFile.jott", 2, TokenType.NUMBER));
            tokens2.add(new Token(">", "testFile.jott", 2, TokenType.REL_OP));
            tokens2.add(new Token("id", "testFile.jott", 2, TokenType.ID_KEYWORD));

            BinaryOpNode binaryOpNode2 = BinaryOpNode.parse(tokens2);
            System.out.println("Parsing BinaryOpNode (5 > id)");
            binaryOpNode2.validateTree(symbolTable); // Should throw a SemanticError for undefined id_keyword type
            System.out.println("Validation passed for (5 > id)");

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println(e.getMessage());
        }
    }
}

    //     try {
    //         // Test Case 3: Math operator with division by zero "10 / 0"
    //         ArrayList<Token> tokens3 = new ArrayList<>();
    //         tokens3.add(new Token("10", "testFile.jott", 3, TokenType.NUMBER));
    //         tokens3.add(new Token("/", "testFile.jott", 3, TokenType.MATH_OP));
    //         tokens3.add(new Token("0", "testFile.jott", 3, TokenType.NUMBER));

    //         BinaryOpNode binaryOpNode3 = BinaryOpNode.parse(tokens3);
    //         System.out.println("Parsing BinaryOpNode (10 / 0)");
    //         binaryOpNode3.validateTree(symbolTable); // Should throw a SemanticError for division by zero
    //         System.out.println("Validation passed for (10 / 0)");

    //     } catch (Exception e) {
    //         System.err.println(e.getMessage());
    //     }

    //     try {
    //         // Test Case 4: Chaining of mathematical operations "3 + 2 * 3"
    //         ArrayList<Token> tokens4 = new ArrayList<>();
    //         tokens4.add(new Token("3", "testFile.jott", 4, TokenType.NUMBER));
    //         tokens4.add(new Token("+", "testFile.jott", 4, TokenType.MATH_OP));
    //         tokens4.add(new Token("2", "testFile.jott", 4, TokenType.NUMBER));
    //         tokens4.add(new Token("*", "testFile.jott", 4, TokenType.MATH_OP));
    //         tokens4.add(new Token("3", "testFile.jott", 4, TokenType.NUMBER));

    //         // Parse this chained expression
    //         System.out.println("Parsing BinaryOpNode (3 + 2 * 3)");
    //         BinaryOpNode binaryOpNode4 = BinaryOpNode.parse(tokens4); // Should throw a SyntaxError for chained
    //                                                                   // operations
    //         binaryOpNode4.validateTree(symbolTable);
    //         System.out.println("Validation passed for (3 + 2 * 3)");

    //     } catch (Exception e) {
    //         System.err.println(e.getMessage());
    //     }

    //     try {
    //         // Test Case 5: Mismatched types for math operation "5 + 3.0"
    //         ArrayList<Token> tokens5 = new ArrayList<>();
    //         tokens5.add(new Token("5", "testFile.jott", 5, TokenType.NUMBER)); // Assume type INTEGER
    //         tokens5.add(new Token("+", "testFile.jott", 5, TokenType.MATH_OP));
    //         tokens5.add(new Token("3.0", "testFile.jott", 5, TokenType.NUMBER)); // Assume type DOUBLE

    //         BinaryOpNode binaryOpNode5 = BinaryOpNode.parse(tokens5);
    //         System.out.println("Parsing BinaryOpNode (5 + 3.0)");
    //         binaryOpNode5.validateTree(symbolTable); // Should throw a SemanticError for mismatched data types
    //         System.out.println("Validation passed for (5 + 3.0)");

    //     } catch (Exception e) {
    //         System.err.println(e.getMessage());
    //     }
    //     System.out.println("-----Finished testing BinaryOpNode.-----");
    // }