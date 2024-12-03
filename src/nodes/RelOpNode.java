package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/**
 * RelOpNode
 * relational operation like >, <, >=, <=, ==, !=
 */
public class RelOpNode implements OperatorNode {

    Token operator;

    public RelOpNode(Token operator) {
        this.operator = operator;
    }

    // Returns Relational Operator Node if a valid relop
    // Otherwise Throws SyntaxError Exception
    public static RelOpNode parse(ArrayList<Token> tokens) throws Exception {

        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected relational operator got " + JottParser.finalToken.getToken(),
                    JottParser.finalToken);
        }
        Token currentToken = tokens.get(0);
        // Make sure token is type REL_OP
        if (currentToken.getTokenType() != TokenType.REL_OP) {
            throw new SyntaxError("Expected relational operator got " + currentToken.getTokenType(), currentToken);
        }
        // Make sure token is a valid operator character
        if (!(currentToken.getToken().equals(">") ||
                currentToken.getToken().equals(">=") ||
                currentToken.getToken().equals("<") ||
                currentToken.getToken().equals("<=") ||
                currentToken.getToken().equals("==") ||
                currentToken.getToken().equals("!="))) {
            throw new SyntaxError("Expected relational operator >, <, >=, <=, == or != got " + currentToken.getToken(),
                    currentToken);
        }

        Token operator = tokens.remove(0);
        return new RelOpNode(operator);
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
        return DataType.BOOLEAN;
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
    
        DataType operandType = leftOperand.type;
        boolean result;
    
        try {
            // Perform the relational operation based on the operand type
            if (operandType == DataType.INTEGER) {
                int leftValue = Integer.parseInt(leftOperand.value);
                int rightValue = Integer.parseInt(rightOperand.value);
    
                switch (operator.getToken()) {
                    case ">":
                        result = leftValue > rightValue;
                        break;
                    case ">=":
                        result = leftValue >= rightValue;
                        break;
                    case "<":
                        result = leftValue < rightValue;
                        break;
                    case "<=":
                        result = leftValue <= rightValue;
                        break;
                    case "==":
                        result = leftValue == rightValue;
                        break;
                    case "!=":
                        result = leftValue != rightValue;
                        break;
                    default:
                        throw new RuntimeException("Unsupported relational operator: " + operator.getToken());
                }
            } else if (operandType == DataType.DOUBLE) {
                double leftValue = Double.parseDouble(leftOperand.value);
                double rightValue = Double.parseDouble(rightOperand.value);
    
                switch (operator.getToken()) {
                    case ">":
                        result = leftValue > rightValue;
                        break;
                    case ">=":
                        result = leftValue >= rightValue;
                        break;
                    case "<":
                        result = leftValue < rightValue;
                        break;
                    case "<=":
                        result = leftValue <= rightValue;
                        break;
                    case "==":
                        result = leftValue == rightValue;
                        break;
                    case "!=":
                        result = leftValue != rightValue;
                        break;
                    default:
                        throw new RuntimeException("Unsupported relational operator: " + operator.getToken());
                }
            } else {
                throw new RuntimeException("Unsupported data type for relational operation: " + operandType);
            }
    
            // Store the result as a Boolean in the symbol table
            symbolTable.addVar(new VarInfo("result", DataType.BOOLEAN, Boolean.toString(result)));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse operand values for relational operation.", e);
        }
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