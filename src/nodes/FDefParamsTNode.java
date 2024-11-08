package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Function Definition Parameters T Node
 * ,<id >: < type >
 */
public class FDefParamsTNode implements JottTree {
    
    IDNode id;
    TypeNode type;

    public FDefParamsTNode(TypeNode type, IDNode id) {
        this.type = type;
        this.id = id;

    }

    // Parsing
    public static FDefParamsTNode parse(ArrayList<Token> tokens) throws Exception {
        // Check if there is tokens
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected comma , got " + JottParser.finalToken.getToken(), JottParser.finalToken);            
        }
        // Check that there are at least 4 tokens
        if (tokens.size() < 4) {
            throw new SyntaxError("Expected at least 4 tokens for Function Definition Parameters T", JottParser.finalToken);
        }

        // Parse comma
        Token comma = tokens.remove(0);
        if (comma.getTokenType() != TokenType.COMMA) {
            throw new SyntaxError("Expected comma , got " + comma.getToken(), comma);
        }

        // Parse the ID
        IDNode id = IDNode.parse(tokens);

        // Parse colon
        Token colon = tokens.remove(0);
        if (colon.getTokenType() != TokenType.COLON) {
            throw new SyntaxError("Expected colon : got " + colon.getToken(), colon);
        }

        // Parse the type
        TypeNode type = TypeNode.parse(tokens);
        return new FDefParamsTNode(type, id);
    }

    @Override
    public String convertToJott() {
        return "," + id.convertToJott() + ":" + type.convertToJott();
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

            System.out.println("Testing FDefParamsTNode Main Method");

            // Test Case 1: ,var:Void
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));
            tokens1.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
            tokens1.add(new Token("Void", "testFile.jott", 1, TokenType.ID_KEYWORD));
            FDefParamsTNode FDefParamsTNode1 = FDefParamsTNode.parse(tokens1);
            System.out.println("Parsed FDefParamsTNode ',var:Void':   " + FDefParamsTNode1.convertToJott());

            // Test Case 2: ,var:Double
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));
            tokens2.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
            tokens2.add(new Token("Double", "testFile.jott", 1, TokenType.ID_KEYWORD));
            FDefParamsTNode FDefParamsTNode2 = FDefParamsTNode.parse(tokens2);
            System.out.println("Parsed FDefParamsTNode ',var:Double':   " + FDefParamsTNode2.convertToJott());

            // Invalid Test Case 3: ,var:
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));
            tokens3.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens3.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
            try {
                FDefParamsTNode FDefParamsTNode3 = FDefParamsTNode.parse(tokens3);
                System.out.println("Parsed FDefParamsTNode ',var:':   " + FDefParamsTNode3.convertToJott()); // Should fail
            } catch (SyntaxError e) {
                System.err.println(e.getMessage());
            }
            

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}