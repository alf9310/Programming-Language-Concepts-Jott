package nodes;

import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

/*
 * Assignment Node
 */
public class AssignmentNode implements BodyStmtNode {

    IDNode id;
    ExpressionNode expression;

    public AssignmentNode(IDNode id, ExpressionNode expression) {
        this.id = id;
        this.expression = expression;
    }

    // Parsing
    public static AssignmentNode parse(ArrayList<Token> tokens) throws Exception {
        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Invalid. Empty token list for Assignment");
        }
        // Check that there are at least 4 tokens
        if (tokens.size() < 4) {
            throw new SyntaxError("Invalid. Expected at least 4 tokens for Assignment");
        }

        // Parse the ID
        IDNode id = IDNode.parse(tokens);
        // Parse assignment operator
        Token assignment = tokens.remove(0);
        if (assignment.getTokenType() != TokenType.ASSIGN) {
            throw new SyntaxError("Invalid. Expected assignment", assignment);
        }
        // Parse the expression
        ExpressionNode expression = ExpressionNode.parse(tokens);
        // Parse semicolon
        Token semicolon = tokens.remove(0);
        if (semicolon.getTokenType() != TokenType.SEMICOLON) {
            throw new SyntaxError("Invalid. Expected semicolon", semicolon);
        }
        return new AssignmentNode(id, expression);
    }

    @Override
    public String convertToJott() {
        return id.convertToJott() + "=" + expression.convertToJott() + ";";
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

            System.out.println("Testing AssignmentNode Main Method");

            // Test Case 1:  var = 4;
            ArrayList<Token> tokens1 = new ArrayList<>();
            // type token
            tokens1.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token("=", "testFile.jott", 1, TokenType.ASSIGN));
            tokens1.add(new Token("4", "testFile.jott", 1, TokenType.NUMBER));
            tokens1.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            
            AssignmentNode AssignmentNode1 = AssignmentNode.parse(tokens1);
            System.out.println("Parsed AssignmentNode 'var=4;' :   " + AssignmentNode1.convertToJott());

            // Test Case 2:  var = 4 + 5;
            ArrayList<Token> tokens2 = new ArrayList<>();
            // type token
            tokens2.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token("=", "testFile.jott", 1, TokenType.ASSIGN));
            tokens2.add(new Token("4", "testFile.jott", 1, TokenType.NUMBER));
            tokens2.add(new Token("+", "testFile.jott", 1, TokenType.MATH_OP));
            tokens2.add(new Token("5", "testFile.jott", 1, TokenType.NUMBER));
            tokens2.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            AssignmentNode AssignmentNode2 = AssignmentNode.parse(tokens2);
            System.out.println("Parsed AssignmentNode 'var=4+5;' :   " + AssignmentNode2.convertToJott());

            // Test Case 3:  var = 4++5; Invalid
            ArrayList<Token> tokens3 = new ArrayList<>();
            // type token
            tokens3.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens3.add(new Token("=", "testFile.jott", 1, TokenType.ASSIGN));
            tokens3.add(new Token("4", "testFile.jott", 1, TokenType.NUMBER));
            tokens3.add(new Token("+", "testFile.jott", 1, TokenType.MATH_OP));
            tokens3.add(new Token("+", "testFile.jott", 1, TokenType.MATH_OP));
            tokens3.add(new Token("5", "testFile.jott", 1, TokenType.NUMBER));
            tokens3.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            try {
                AssignmentNode AssignmentNode3 = AssignmentNode.parse(tokens3);
                System.out.println("Parsed AssignmentNode 'var=4++5;' :   " + AssignmentNode3.convertToJott()); // Should throw an error
            } catch (Exception e) {
                System.err.println(e.getMessage()); // Expected error: Invalid token
            }

            // Test Case 3: var = 4+ -5; Valid
            ArrayList<Token> tokens4 = new ArrayList<>();
            // type token
            tokens4.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens4.add(new Token("=", "testFile.jott", 1, TokenType.ASSIGN));
            tokens4.add(new Token("4", "testFile.jott", 1, TokenType.NUMBER));
            tokens4.add(new Token("+", "testFile.jott", 1, TokenType.MATH_OP));
            tokens4.add(new Token("-", "testFile.jott", 1, TokenType.MATH_OP));
            tokens4.add(new Token("5", "testFile.jott", 1, TokenType.NUMBER));
            tokens4.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            AssignmentNode AssignmentNode4 = AssignmentNode.parse(tokens4);
            System.out.println("Parsed AssignmentNode 'var=4+-5;' :   " + AssignmentNode4.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}