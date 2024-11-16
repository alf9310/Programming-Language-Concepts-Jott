package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import java.util.HashMap;
import msc.*;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Variable Declaration Node
 * < type > < id >;
 */
public class VarDecNode implements JottTree {

    TypeNode type;
    IDNode id;

    public VarDecNode(TypeNode type, IDNode id) {
        this.type = type;
        this.id = id;
    }

    // Parsing
    public static VarDecNode parse(ArrayList<Token> tokens) throws Exception {
        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected type got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }
        // Parse the type
        TypeNode type = TypeNode.parse(tokens);
        // Parse the ID
        IDNode id = IDNode.parse(tokens);
        // Parse semicolon
        if (tokens.isEmpty() || tokens.remove(0).getTokenType() != TokenType.SEMICOLON) {
            throw new SyntaxError(
                    "Variable Declaration missing closing semicolon ; got " + JottParser.finalToken.getToken(),
                    JottParser.finalToken);
        }
        return new VarDecNode(type, id);
    }

    @Override
    public String convertToJott() {
        return type.convertToJott() + " " + id.convertToJott() + ";";
    }

    /*
    Type is valid 
    Id is valid 
    Must be unique in the function scope
    Also store type in scope to validate that type matches the expression where it’s being used
    */
    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        // System.out.println("HERE");
        type.validateTree(symbolTable);

        // Ensure variable is unique in the current scope
        if (symbolTable.existsInScope(id.convertToJott())) {
            throw new RuntimeException("Variable '" + id.convertToJott() + "' is already defined in the current scope.");
        }

        // Adds variable to symbol table 
        DataType dataType = type.getType();
        VarInfo variable = new VarInfo(id.convertToJott(), dataType, null);
        Boolean succeeded = symbolTable.addVar(variable);
        // System.out.println("from VarDecNode: " + symbolTable.current_scope);
        if (!succeeded) {
            throw new UnsupportedOperationException("failed to add var");
        }

        id.validateTree(symbolTable);

        return true;
    }

    @Override
    public void execute() {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }

    public static void main(String[] args) {
        try {
            System.out.println("Testing VarDecNode Main Method");

            SymbolTable symbolTable = new SymbolTable();

            // Test Case 1: Valid variable declaration in new scope
            symbolTable.addFunction("main", new FunctionInfo("main", "void", new HashMap<>()));
            Boolean succeeded = symbolTable.enterScope("main");
            if (!succeeded) {
                throw new UnsupportedOperationException("failed to enter scope!");
            }
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("Boolean", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));

            VarDecNode varDecNode1 = VarDecNode.parse(tokens1);
            System.out.println("Parsed VarDecNode (Boolean var;):   " + varDecNode1.convertToJott());
            boolean isValid1 = varDecNode1.validateTree(symbolTable);
            System.out.println("Validation result (Boolean var;):   " + isValid1); // Expected: true

            // Test Case 2: Duplicate variable declaration (should throw an error)
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("Boolean", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));  // Duplicate name
            tokens2.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));

            VarDecNode varDecNode2 = VarDecNode.parse(tokens2);
            System.out.println("Parsed VarDecNode (Boolean var;):   " + varDecNode2.convertToJott());
            try {
                varDecNode2.validateTree(symbolTable);
                System.out.println("Validation result (Boolean var;): Passed"); // Should throw an error
            } catch (RuntimeException e) {
                System.err.println("Expected error: " + e.getMessage());
            }

            // Test Case 3: Valid variable declaration with a new variable name
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token("String", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens3.add(new Token("newVar", "testFile.jott", 1, TokenType.ID_KEYWORD)); // New name
            tokens3.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));

            VarDecNode varDecNode3 = VarDecNode.parse(tokens3);
            System.out.println("Parsed VarDecNode (String newVar;):   " + varDecNode3.convertToJott());
            boolean isValid3 = varDecNode3.validateTree(symbolTable);
            System.out.println("Validation result (String newVar;):   " + isValid3); // Expected: true

            // Test Case 4: Variable declared outside any scope (should throw an error)
            symbolTable.exitScope(); // Exit the current scope

            ArrayList<Token> tokens4 = new ArrayList<>();
            tokens4.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens4.add(new Token("outOfScopeVar", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens4.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));

            VarDecNode varDecNode4 = VarDecNode.parse(tokens4);
            System.out.println("Parsed VarDecNode (Integer outOfScopeVar;):   " + varDecNode4.convertToJott());
            try {
                varDecNode4.validateTree(symbolTable);
                System.out.println("Validation result (Integer outOfScopeVar;): Passed"); // Should throw an error
            } catch (RuntimeException e) {
                System.err.println("Expected error: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}