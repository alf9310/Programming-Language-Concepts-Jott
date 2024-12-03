package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Else Node
 * Else { < body >} | ε
 */
public class ElseNode implements JottTree {
    BodyNode body;

    public ElseNode() {
        this.body = null;
    }

    public ElseNode(BodyNode bodyNode) {
        this.body = bodyNode;
    }

    public DataType getReturnType() throws Exception {
        if(this.body == null) {
            return null;
        }
        return this.body.getReturnType();
    }

    public boolean allReturn() {
        if(this.body == null) {
            return false;
        }
        return this.body.allReturn();
    }

    public Token getToken() {
        if(this.body == null) {
            return null;
        }
        return this.body.getToken();
    }

    public static ElseNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            return new ElseNode();
        }

        Token currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.ID_KEYWORD || !currentToken.getToken().equals("Else")) {
            // throw new SyntaxError("Expected Else got " + currentToken.getToken(), currentToken);
            return new ElseNode();  // next token probably part of a different body statement
        }
        tokens.remove(0);

        if (tokens.isEmpty()) {
            throw new SyntaxError("Else statement is missing a left brace {", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxError("Else statement missing a left brace {, instead got " + currentToken.getTokenType(),
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

        return new ElseNode(body);
    }

    @Override
    public String convertToJott() {
        if (this.body != null) {
            return "Else{ " + this.body.convertToJott() + " }";
        } else {
            return "";
        }
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        // To be implemented in phase 3
        if(this.body != null) {
            return this.body.validateTree(symbolTable);
        }
        return true;
    }

    @Override
    public Object execute(SymbolTable symbolTable) throws Exception {
        // To be implemented in phase 4
        //throw new UnsupportedOperationException("Execution not supported yet.");
        if(this.body == null) {
            return null;
        } else {
            return this.body.execute(symbolTable);
        }
    }

    public static void main(String[] args) {
        /*
        System.out.println("Testing ElseNode validateTree Method");

        // Initialize SymbolTable
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
        symbolTable.enterScope("main");
        symbolTable.addFunction("func1", new FunctionInfo("func1", "Integer", new HashMap<>()));
        symbolTable.enterScope("func1");

        // Test Case 1: Valid ElseNode
        try {
            System.out.println("\nTest Case 1: Valid ElseNode");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Else", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Else
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("Return", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Return
            tokens.add(new Token("42", "testFile.jott", 1, TokenType.NUMBER)); // someBody
            tokens.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON)); // ;
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE)); // }

            // Parse the ElseNode
            ElseNode elseNode = ElseNode.parse(tokens);

            boolean isValid = elseNode.validateTree(symbolTable);
            System.out.println("Validation Result: " + isValid);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Test Case 2: Invalid ElseNode (Missing Left Brace)
        try {
            System.out.println("\nTest Case 2: Invalid ElseNode (Missing Left Brace)");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Else", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Else
            tokens.add(new Token("Return", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Return
            tokens.add(new Token("42", "testFile.jott", 1, TokenType.NUMBER)); // someBody
            tokens.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON)); // ;
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE)); // }
            // Missing left brace token

            // Parse the ElseNode
            ElseNode elseNode = ElseNode.parse(tokens);

            // This should throw an error when trying to validate
            elseNode.validateTree(symbolTable);
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }

        // Test Case 3: Invalid Return Type
        try {
            System.out.println("\nTest Case 3: Invalid Return Type");
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token("Else", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Else
            tokens.add(new Token("{", "testFile.jott", 1, TokenType.L_BRACE)); // {
            tokens.add(new Token("Return", "testFile.jott", 1, TokenType.ID_KEYWORD)); // Return
            tokens.add(new Token("\"Hello\"", "testFile.jott", 1, TokenType.STRING)); // "
            tokens.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON)); // ;
            tokens.add(new Token("}", "testFile.jott", 1, TokenType.R_BRACE)); // }

            // Parse the ElseNode
            ElseNode elseNode = ElseNode.parse(tokens);

            boolean isValid = elseNode.validateTree(symbolTable);
            System.out.println("Validation Result: " + isValid);
        } catch (Exception e) {
            System.err.println("Expected Error: " + e.getMessage());
        }
    */
    }
}
