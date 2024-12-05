package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import java.util.HashMap;
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
    public DataType getType(SymbolTable symbolTable) throws Exception {
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
    public Object execute(SymbolTable symbolTable) throws Exception {
        // Execute left and right operands to get their values
        Object leftValue = leftOperand.execute(symbolTable);
        Object rightValue = rightOperand.execute(symbolTable);

        // Perform the operation using the operator
        return operator.execute(symbolTable, leftValue, rightValue);
    }

    public static void main(String[] args) {
        try {
            System.out.println("-----Testing BinaryOpNode Main Method-----");
   
            // Initialize the SymbolTable
            SymbolTable symbolTable = new SymbolTable();
            symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
            symbolTable.enterScope("main");
   
            // Add a test variable to the SymbolTable
            symbolTable.addVar(new VarInfo("id", DataType.INTEGER, "5")); // Define "id" as an integer in the SymbolTable
            symbolTable.addVar(new VarInfo("x", DataType.DOUBLE, "10.5")); // Define "x" as a double in the SymbolTable
   
            // Test Case 1: Valid mathematical operation "5 + 3"
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("5", "testFile.jott", 1, TokenType.NUMBER));
            tokens1.add(new Token("+", "testFile.jott", 1, TokenType.MATH_OP));
            tokens1.add(new Token("3", "testFile.jott", 1, TokenType.NUMBER));
   
            BinaryOpNode binaryOpNode1 = BinaryOpNode.parse(tokens1);
            System.out.println("Parsing BinaryOpNode (5 + 3): " + binaryOpNode1.convertToJott());
            binaryOpNode1.validateTree(symbolTable); // Should pass without error
            System.out.println("Validation passed for (5 + 3)");
   
            // Test Case 2: Valid relational operator "5 > id"
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("5", "testFile.jott", 2, TokenType.NUMBER));
            tokens2.add(new Token(">", "testFile.jott", 2, TokenType.REL_OP));
            tokens2.add(new Token("id", "testFile.jott", 2, TokenType.ID_KEYWORD));
   
            BinaryOpNode binaryOpNode2 = BinaryOpNode.parse(tokens2);
            System.out.println("Parsing BinaryOpNode (5 > id): " + binaryOpNode2.convertToJott());
            binaryOpNode2.validateTree(symbolTable); // Should pass without error
            System.out.println("Validation passed for (5 > id)");
   
            // Test Case 3: Division by zero "5 / 0"
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token("5", "testFile.jott", 3, TokenType.NUMBER));
            tokens3.add(new Token("/", "testFile.jott", 3, TokenType.MATH_OP));
            tokens3.add(new Token("0", "testFile.jott", 3, TokenType.NUMBER));
   
            BinaryOpNode binaryOpNode3 = BinaryOpNode.parse(tokens3);
            System.out.println("Parsing BinaryOpNode (5 / 0): " + binaryOpNode3.convertToJott());
            try {
                binaryOpNode3.validateTree(symbolTable); // Should throw a SemanticError
            } catch (SemanticError e) {
                System.err.println("Caught SemanticError as expected: " + e.getMessage());
            }
   
            // Test Case 4: Mismatched operand types in mathematical operation "5 + x"
            ArrayList<Token> tokens4 = new ArrayList<>();
            tokens4.add(new Token("5", "testFile.jott", 4, TokenType.NUMBER));
            tokens4.add(new Token("+", "testFile.jott", 4, TokenType.MATH_OP));
            tokens4.add(new Token("x", "testFile.jott", 4, TokenType.ID_KEYWORD));
   
            BinaryOpNode binaryOpNode4 = BinaryOpNode.parse(tokens4);
            System.out.println("Parsing BinaryOpNode (5 + x): " + binaryOpNode4.convertToJott());
            try {
                binaryOpNode4.validateTree(symbolTable); // Should throw a SemanticError
            } catch (SemanticError e) {
                System.err.println("Caught SemanticError as expected: " + e.getMessage());
            }
   
            // Test Case 5: Chained math operations "5 + 3 * 2"
            ArrayList<Token> tokens5 = new ArrayList<>();
            tokens5.add(new Token("5", "testFile.jott", 5, TokenType.NUMBER));
            tokens5.add(new Token("+", "testFile.jott", 5, TokenType.MATH_OP));
            tokens5.add(new Token("3", "testFile.jott", 5, TokenType.NUMBER));
            tokens5.add(new Token("*", "testFile.jott", 5, TokenType.MATH_OP));
            tokens5.add(new Token("2", "testFile.jott", 5, TokenType.NUMBER));
   
            try {
                BinaryOpNode binaryOpNode5 = BinaryOpNode.parse(tokens5);
                System.out.println("Parsing BinaryOpNode (5 + 3 * 2): " + binaryOpNode5.convertToJott());
                binaryOpNode5.validateTree(symbolTable); // Should throw a SyntaxError
            } catch (SyntaxError e) {
                System.err.println("Caught SyntaxError as expected: " + e.getMessage());
            }
   
            // Test Case 6: Undefined identifier "undefined_id + 5"
            ArrayList<Token> tokens6 = new ArrayList<>();
            tokens6.add(new Token("undefined_id", "testFile.jott", 6, TokenType.ID_KEYWORD));
            tokens6.add(new Token("+", "testFile.jott", 6, TokenType.MATH_OP));
            tokens6.add(new Token("5", "testFile.jott", 6, TokenType.NUMBER));
   
            try {
                BinaryOpNode binaryOpNode6 = BinaryOpNode.parse(tokens6);
                System.out.println("Parsing BinaryOpNode (undefined_id + 5): " + binaryOpNode6.convertToJott());
                binaryOpNode6.validateTree(symbolTable); // Should throw a SemanticError
            } catch (Exception e) {
                System.err.println("Caught RuntimeError as expected: " + e.getMessage());
            }
   
        } catch (Exception e) {
            // Catch and print any unexpected exceptions
            System.err.println("Unexpected exception: " + e.getMessage());
        }
    }
}