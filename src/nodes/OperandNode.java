package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import msc.DataType;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/*
 * Operand Node
 * <id> | <num> | <func_call> | -<num>
 */
public interface OperandNode extends ExpressionNode {

    // Determine if the node is IDNode, (-)NumberNode, or FunctionCallNode. 
    // Returns the proper node type that you created above.
    public static OperandNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("Expected id, num, function call or - num got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }

        Token currentToken = tokens.get(0);

        // <id>
        if(currentToken.getTokenType() == TokenType.ID_KEYWORD){
	        return IDNode.parse(tokens);
        // <num>
        } else if(currentToken.getTokenType() == TokenType.NUMBER){
	        return NumberNode.parse(tokens);
        // <func_call>
        } else if(currentToken.getTokenType() == TokenType.FC_HEADER){
	        return FunctionCallNode.parse(tokens);
        // -<num>
        } else if (currentToken.getTokenType() == TokenType.MATH_OP && currentToken.getToken().equals("-") 
        && tokens.size() > 1 && tokens.get(1).getTokenType() == TokenType.NUMBER) {
            return NumberNode.parse(tokens); // Negation is handled in NumberNode's parse method
        }
        throw new SyntaxError("Expected ID_KEYWORD, (-)NUMBER or FC_HEADER for Operand got " + currentToken.getToken(), currentToken);
    }

    public DataType getType();

    public Token getToken();

    public static void main(String[] args) {
        System.out.println("Testing Operand Node Main Method");
        try {
            // Test Case 1: Valid number
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("42", "testFile.jott", 1, TokenType.NUMBER));
            OperandNode operandNode1 = OperandNode.parse(tokens1);
            System.out.println("Parsed OperandNode (42): " + operandNode1.convertToJott());

            // Test Case 2: Valid identifier
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("myVar", "testFile.jott", 2, TokenType.ID_KEYWORD));
            OperandNode operandNode2 = OperandNode.parse(tokens2);
            System.out.println("Parsed OperandNode (myVar): " + operandNode2.convertToJott());

            // Test Case 3: Valid function call
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token("::", "testFile.jott", 3, TokenType.FC_HEADER)); // Function call header
            tokens3.add(new Token("myFunction", "testFile.jott", 3, TokenType.ID_KEYWORD)); // Function name
            tokens3.add(new Token("[", "testFile.jott", 3, TokenType.L_BRACKET)); // Opening bracket for parameters
            tokens3.add(new Token("param1", "testFile.jott", 3, TokenType.ID_KEYWORD)); // Parameter 1
            tokens3.add(new Token(",", "testFile.jott", 3, TokenType.COMMA)); // Comma separating parameters
            tokens3.add(new Token("param2", "testFile.jott", 3, TokenType.ID_KEYWORD)); // Parameter 2
            tokens3.add(new Token("]", "testFile.jott", 3, TokenType.R_BRACKET)); // Closing bracket for parameters
            OperandNode operandNode3 = OperandNode.parse(tokens3);
            System.out.println("Parsed OperandNode (myFunction): " + operandNode3.convertToJott());

            // Test Case 4: Valid negated number
            ArrayList<Token> tokens4 = new ArrayList<>();
            tokens4.add(new Token("-", "testFile.jott", 4, TokenType.MATH_OP));
            tokens4.add(new Token("7", "testFile.jott", 4, TokenType.NUMBER));
            OperandNode operandNode4 = OperandNode.parse(tokens4);
            System.out.println("Parsed OperandNode (-7): " + operandNode4.convertToJott());

            // Test Case 5: Invalid operand (not a number, identifier, or function call)
            ArrayList<Token> tokens5 = new ArrayList<>();
            tokens5.add(new Token("!", "testFile.jott", 5, TokenType.MATH_OP)); // Invalid token
            try {
                OperandNode operandNode5 = OperandNode.parse(tokens5);
                System.out.println("Parsed OperandNode: " + operandNode5.convertToJott());
            } catch (Exception e) {
                System.err.println("Test 5 Failed: " + e.getMessage());
            }

            // Test Case 6: Invalid identifier (not starting with a lowercase letter)
            ArrayList<Token> tokens6 = new ArrayList<>();
            tokens6.add(new Token("MyVar", "testFile.jott", 6, TokenType.ID_KEYWORD)); // Invalid identifier
            try {
                OperandNode operandNode6 = OperandNode.parse(tokens6);
                System.out.println("Parsed OperandNode: " + operandNode6.convertToJott());
            } catch (Exception e) {
                System.err.println("Test 6 Failed: " + e.getMessage());
            }

        } catch (Exception e) {
            // Catch and print any exceptions that occur during testing
            System.err.println("Error: " + e.getMessage());
        }
    }
}