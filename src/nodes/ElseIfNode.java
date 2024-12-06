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
    Boolean runElseIf;

    // constructor
    public ElseIfNode(ExpressionNode exprNode, BodyNode bodyNode) {
        this.expr = exprNode;
        this.body = bodyNode;
        this.runElseIf = false;
    }

    public DataType getReturnType() throws Exception {
        return this.body.getReturnType();
    }

    public boolean allReturn() {
        return this.body.allReturn();
    }

    public Boolean runs() {
        return this.runElseIf;
    }

    public Token getToken() {
        if(this.body.getToken() != null) {
            return this.body.getToken();
        } else {
            return this.expr.getToken();
        }
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
        this.expr.validateTree(symbolTable);
        this.body.validateTree(symbolTable);
        
        if(this.expr.getType(symbolTable) != DataType.BOOLEAN) {
            throw new SemanticError("Expression in if statement must be a boolean", this.expr.getToken());
        }
        return true;
    }

    @Override
    public Object execute(SymbolTable symbolTable) throws Exception {
        // To be implemented in phase 4
        Object runCurrent = this.expr.execute(symbolTable);
        if(runCurrent instanceof String) {
            if(runCurrent.toString().equalsIgnoreCase("true")) {
                this.runElseIf = true;
                return this.body.execute(symbolTable);
            }
        } else if((Boolean)runCurrent) {
            this.runElseIf = true;
            return this.body.execute(symbolTable);
        }
        return null;
    }

    public static void main(String[] args) {
        /*
        System.out.println("Testing ElseIfNode Main Method");

        // Initialize SymbolTable
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
        symbolTable.enterScope("main");
        symbolTable.addFunction("func1", new FunctionInfo("func1", "Integer", new HashMap<>()));
        symbolTable.enterScope("func1");
    
        // Valid Test Case
        try {
            System.out.println("\nTest Case 1: Valid Elseif");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Elseif", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Elseif
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("True", "testFile.jott", 1, TokenType.ID_KEYWORD)); // True
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("Return", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Return
            tokens.add(new Token("42", "testFile.jott", 1, TokenType.NUMBER)); // someBody
            tokens.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON)); // ;
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE)); // }
    
            // Parse the ElseIfNode
            ElseIfNode elseIfNode = ElseIfNode.parse(tokens);
    
            boolean isValid = elseIfNode.validateTree(symbolTable);
            System.out.println("Validation Result: " + isValid);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    
        // Failing Test Case 1: Non-Boolean Expression
        try {
            System.out.println("\nTest Case 2: Non-Boolean Expression");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Elseif", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Elseif
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("42", "testFile.jott", 1, TokenType.NUMBER)); // 42 (not a boolean)
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("Return", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Return
            tokens.add(new Token("1", "testFile.jott", 1, TokenType.NUMBER)); // someBody
            tokens.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON)); // ;
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE)); // }
    
            ElseIfNode elseIfNode = ElseIfNode.parse(tokens);

            elseIfNode.validateTree(symbolTable); // This should throw an error
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }
    
        // Failing Test Case 2: Invalid Body Return
        try {
            System.out.println("\nTest Case 3: Invalid Body Return");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Elseif", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Elseif
            tokens.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens.add(new Token("true", "testFile.jott", 1, TokenType.ID_KEYWORD)); // true
            tokens.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("Return", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Return
            tokens.add(new Token("42.5", "testFile.jott", 1, TokenType.NUMBER)); // Invalid type
            tokens.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON)); // ;
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE)); // }
    
            ElseIfNode elseIfNode = ElseIfNode.parse(tokens);

            elseIfNode.validateTree(symbolTable); // This should throw an error
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }
    */
    }
}
