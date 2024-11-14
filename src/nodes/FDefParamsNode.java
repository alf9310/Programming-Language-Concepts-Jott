package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import provided.JottTree;
import provided.Token;
import provided.TokenType;
import msc.*;

/*
 * Function Definition Parameters Node
 * <id >: < type > < function_def_params_t >⋆ | ε
 */
public class FDefParamsNode implements JottTree {
    IDNode id;
    TypeNode type;
    ArrayList<FDefParamsTNode> fDefParamsTNode;

    public FDefParamsNode() {
        this.type = null;
        this.id = null;
        this.fDefParamsTNode = null;

    }

    public FDefParamsNode(TypeNode type, IDNode id, ArrayList<FDefParamsTNode> fDefParamsTNode) {
        this.type = type;
        this.id = id;
        this.fDefParamsTNode = fDefParamsTNode;
    }

    // Parsing
    public static FDefParamsNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            return new FDefParamsNode();
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

        // <FDefParamsTNode>⋆
        ArrayList<FDefParamsTNode> list = new ArrayList<>();

        if (tokens.isEmpty()) {
            return new FDefParamsNode(type, id, list);
        }

        Token currentToken = tokens.get(0);
        while (!tokens.isEmpty() && currentToken.getTokenType() == TokenType.COMMA) {
            list.add(FDefParamsTNode.parse(tokens));
            if (!tokens.isEmpty()) {
                currentToken = tokens.get(0);
            }
        }
        return new FDefParamsNode(type, id, list);
    }

    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        if (id != null) {
            str.append(id.convertToJott());
            str.append(":");
            str.append(type.convertToJott());
            for (FDefParamsTNode paramt : fDefParamsTNode) {
                str.append(paramt.convertToJott());
            }
        }
        return str.toString();
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) {
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

            System.out.println("Testing FDefParamsNode Main Method");

            // Test Case 1: 'x:Integer'
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("x", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens1.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
            tokens1.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD));
            FDefParamsNode FDefParamsNode1 = FDefParamsNode.parse(tokens1);
            System.out.println("Parsed FDefParamsNode 'x:Integer':   " + FDefParamsNode1.convertToJott());

            // Test Case 2: 'x:Integer, y:Double'
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("x", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
            tokens2.add(new Token("Integer", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));
            tokens2.add(new Token("y", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token(":", "testFile.jott", 1, TokenType.COLON));
            tokens2.add(new Token("Double", "testFile.jott", 1, TokenType.ID_KEYWORD));
            FDefParamsNode FDefParamsNode2 = FDefParamsNode.parse(tokens2);
            System.out.println("Parsed FDefParamsNode 'x:Integer, y:Double':   " + FDefParamsNode2.convertToJott());

            // Test Case 3 Empty: ''
            ArrayList<Token> tokens3 = new ArrayList<>();
            FDefParamsNode FDefParamsNode3 = FDefParamsNode.parse(tokens3);
            System.out.println("Parsed FDefParamsNode '':   " + FDefParamsNode3.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}