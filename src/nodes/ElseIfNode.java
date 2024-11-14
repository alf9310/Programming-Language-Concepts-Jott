package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Else If Node
 * Elseif [ < expr >]{ < body >}
 */
public class ElseIfNode implements JottTree {
    // vars
    ExpressionNode expr;
    BodyNode body;

    // constructor
    public ElseIfNode(ExpressionNode exprNode, BodyNode bodyNode) {
        this.expr = exprNode;
        this.body = bodyNode;
    }

    public DataType getReturnType() throws Exception {
        return this.body.getReturnType();
    }

    // parse
    public static ElseIfNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected Elseif got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }

        Token currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.ID_KEYWORD || !currentToken.getToken().equals("Elseif")) {
            throw new SyntaxError("Expected Elseif got " + currentToken.getToken(), currentToken);
        }
        tokens.remove(0);

        if (tokens.isEmpty()) {
            throw new SyntaxError("Else if statement is missing a left bracket [", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.L_BRACKET) {
            throw new SyntaxError(
                    "Else if statement missing a left bracket [, instead got " + currentToken.getTokenType(),
                    currentToken);
        }
        tokens.remove(0);

        ExpressionNode expr = ExpressionNode.parse(tokens);

        if (tokens.isEmpty()) {
            throw new SyntaxError("Else if statement is missing a right bracket ]", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.R_BRACKET) {
            throw new SyntaxError(
                    "Else if statement missing a right bracket ], instead got " + currentToken.getTokenType(),
                    currentToken);
        }
        tokens.remove(0);

        if (tokens.isEmpty()) {
            throw new SyntaxError("Else if statement is missing a left brace {", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxError(
                    "Else if statement missing a left brace {, instead got " + currentToken.getTokenType(),
                    currentToken);
        }
        tokens.remove(0);

        BodyNode body = BodyNode.parse(tokens);

        if (tokens.isEmpty()) {
            throw new SyntaxError("Else if statement is missing a right brace }", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.R_BRACE) {
            throw new SyntaxError(
                    "Else if statement missing a right brace }, instead got " + currentToken.getTokenType(),
                    currentToken);
        }
        tokens.remove(0);

        return new ElseIfNode(expr, body);
    }

    @Override
    public String convertToJott() {
        String outStr = "Elseif[ " + this.expr.convertToJott() + " ]{ " + this.body.convertToJott() + " }";
        return outStr;
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        // To be implemented in phase 3
        this.expr.validateTree(symbolTable);
        this.body.validateTree(symbolTable);
        if(this.expr.getType(symbolTable) != DataType.BOOLEAN) {
            throw new SemanticError("Expression in if statement must be a boolean", this.expr.getToken());
        }
        return true;
    }

    @Override
    public void execute() {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("Testing ElseIfNode Main Method");

        // Valid Test Case
        try {
            System.out.println("\nTest Case 1: Valid Elseif");
            // Add tokens for Elseif[ someExpression ]{ someBody }
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Elseif", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Elseif
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("someExpression", "testFile.jott", 1, TokenType.ID_KEYWORD)); // someExpression
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("someBody", "testFile.jott", 1, TokenType.ID_KEYWORD)); // someBody
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE)); // }

            // Parse and print result
            ElseIfNode elseIfNode = ElseIfNode.parse(tokens);
            System.out.println("Parsed ElseIfNode: " + elseIfNode.convertToJott());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Failing Test Case 1: Missing "Elseif" keyword
        try {
            System.out.println("\nTest Case 2: Missing Elseif keyword");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // Missing Elseif
            tokens.add(new Token("someExpression", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET));
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE));
            tokens.add(new Token("someBody", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE));

            ElseIfNode.parse(tokens);
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }

        // Failing Test Case 2: Missing Left Bracket
        try {
            System.out.println("\nTest Case 3: Missing Left Bracket");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Elseif", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Elseif
            tokens.add(new Token("someExpression", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Missing [
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET));
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE));
            tokens.add(new Token("someBody", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE));

            ElseIfNode.parse(tokens);
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }

        // Failing Test Case 3: Missing Right Bracket
        try {
            System.out.println("\nTest Case 4: Missing Right Bracket");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Elseif", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Elseif
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("someExpression", "testFile.jott", 1, TokenType.ID_KEYWORD)); // someExpression
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE));
            tokens.add(new Token("someBody", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE));

            ElseIfNode.parse(tokens);
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }

        // Failing Test Case 4: Missing Left Brace
        try {
            System.out.println("\nTest Case 5: Missing Left Brace");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Elseif", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Elseif
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("someExpression", "testFile.jott", 1, TokenType.ID_KEYWORD)); // someExpression
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens.add(new Token("someBody", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Missing {
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE));

            ElseIfNode.parse(tokens);
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }

        // Failing Test Case 5: Missing Right Brace
        try {
            System.out.println("\nTest Case 6: Missing Right Brace");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Elseif", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Elseif
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("someExpression", "testFile.jott", 1, TokenType.ID_KEYWORD)); // someExpression
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("someBody", "testFile.jott", 1, TokenType.ID_KEYWORD)); // someBody

            ElseIfNode.parse(tokens); // Missing }
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }
    }
}
