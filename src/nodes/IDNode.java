package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import msc.*;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/*
 * ID Node
 * An identifier
 */
public class IDNode implements OperandNode {

    Token id;

    public IDNode(Token id) {
        this.id = id;
    }

    // Returns ID Node if an ID
    // Otherwise Throws SyntaxError Exception
    public static IDNode parse(ArrayList<Token> tokens) throws Exception {
        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected id got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }

        Token currentToken = tokens.get(0);
        // Make sure token is type ID_KEYWORD
        if (!(currentToken.getTokenType() == TokenType.ID_KEYWORD)) {
            // System.err.println(currentToken.getTokenType());
            throw new SyntaxError("Expected id or keyword got " + currentToken.getToken(), currentToken);
        }

        List<String> uppercaseAllowed = Arrays.asList("Boolean", "Integer", "String", "Double", "Void", "True",
                "False");
        if (!uppercaseAllowed.contains(currentToken.getToken())) {
            if (Character.isUpperCase(currentToken.getToken().charAt(0))) {
                throw new SyntaxError("First character of id is not lowercase " + currentToken.getToken(),
                        currentToken);
            }
        }

        Token id = tokens.remove(0);
        return new IDNode(id);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return id.getToken();
    }

    @Override
    public DataType getType(SymbolTable symbolTable) throws SemanticError {
        // References scope table to get this
        VarInfo var = symbolTable.getVar(id.getToken());
        if (var == null){
            throw new SemanticError("Variable " + id.getToken() + " not found in current scope", id);
        }
        DataType type = var.type;
        return type;
    }

    @Override
    public Token getToken() {
        return id;
    }

    // Check if ID exists in current scope
    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        if (this.id.getTokenType() == TokenType.ID_KEYWORD) {
            // Check if the variable exists in the current scope
            if (!symbolTable.existsInScope(this.id.getToken()) && !symbolTable.functionMap.containsKey(this.id.getToken())) {
                throw new SemanticError("Undeclared variable: " + this.id.getToken(), this.id);
            } 
        }
        // Additional validation for other operand types, if needed
        return true;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("Testing IDNode Main Method");

        // Test Case 1: Valid ID with lowercase first character
        ArrayList<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token("variableName", "test.jott", 1, TokenType.ID_KEYWORD));
        try {
            IDNode node1 = IDNode.parse(tokens1);
            System.out.println("Parsed IDNode: " + node1.convertToJott()); // Expected: variableName
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Test Case 2: Invalid Token Type (not ID_KEYWORD)
        ArrayList<Token> tokens2 = new ArrayList<>();
        tokens2.add(new Token("123", "test.jott", 2, TokenType.NUMBER));
        try {
            IDNode node2 = IDNode.parse(tokens2);
            System.out.println("Parsed IDNode: " + node2.convertToJott()); // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage()); // Expected error: Id type is not ID_KEYWORD
        }

        // Test Case 3: ID with Uppercase first character (Invalid)
        ArrayList<Token> tokens3 = new ArrayList<>();
        tokens3.add(new Token("VariableName", "test.jott", 3, TokenType.ID_KEYWORD));
        try {
            IDNode node3 = IDNode.parse(tokens3);
            System.out.println("Parsed IDNode: " + node3.convertToJott()); // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage()); // Expected error: First character is not lowercase for id
        }

        // Test Case 4: Empty Token List
        ArrayList<Token> tokens4 = new ArrayList<>();
        try {
            IDNode node4 = IDNode.parse(tokens4);
            System.out.println("Parsed IDNode: " + node4.convertToJott()); // Should throw an error
        } catch (Exception e) {
            System.err.println(e.getMessage()); // Expected error: Empty token list for id
        }
    }
}
