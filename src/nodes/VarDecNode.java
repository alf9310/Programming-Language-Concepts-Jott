package nodes;

import java.util.ArrayList;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Variable Declaration Node
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
            throw new SyntaxError("VarDecNode Error: Empty token list for VarDeclaration");
        }
        // Parse the type
        TypeNode type = TypeNode.parse(tokens);
        // Parse the ID
        IDNode id = IDNode.parse(tokens);
        // Parse semicolon
        if (tokens.isEmpty() || tokens.remove(0).getTokenType() != TokenType.SEMICOLON) {
            throw new SyntaxError("VarDecNode Error: Invalid. Expected semicolon");
        }
        return new VarDecNode(type, id);
    }

    @Override
    public String convertToJott() {
        return type.convertToJott() + " " + id.convertToJott() + ";";
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

            System.out.println("Testing VarDecNode Main Method");

            // Test Case 1: Boolean var; Valid
            ArrayList<Token> tokens1 = new ArrayList<>();
            // type token
            tokens1.add(new Token("Boolean", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            VarDecNode VarDecNode1 = VarDecNode.parse(tokens1);
            System.out.println("Parsed VarDecNode (Boolean var;):   " + VarDecNode1.convertToJott());

            // Test Case 2: ProfJohnson var; Invalid type
            ArrayList<Token> tokens2 = new ArrayList<>();
            // type token
            tokens2.add(new Token("ProfJohnson", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            try {
                VarDecNode VarDecNode2 = VarDecNode.parse(tokens2);
                System.out.println("Parsed VarDecNode (ProfJohnson var;):   " + VarDecNode2.convertToJott()); // Should throw an error
            } catch (Exception e) {
                System.err.println(e.getMessage()); // Expected error: ProfJohnson is not a valid type
            }

            // Test Case 3: String Var; Invalid ID because first character is uppercase
            ArrayList<Token> tokens3 = new ArrayList<>();
            // type token
            tokens3.add(new Token("String", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens3.add(new Token("Var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens3.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            try {
                VarDecNode VarDecNode3 = VarDecNode.parse(tokens3);
                System.out.println("Parsed VarDecNode (ProfJohnson var;):   " + VarDecNode3.convertToJott()); // Should
                                                                                                              // throw
                                                                                                              // an
                                                                                                              // error
            } catch (Exception e) {
                System.err.println(e.getMessage()); // Expected error: ProfJohnson is not a valid type
            }

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}