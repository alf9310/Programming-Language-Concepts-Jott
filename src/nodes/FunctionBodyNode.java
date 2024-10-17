package nodes;

import java.util.ArrayList;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class FuncBodyNode implements JottTree {
    ArrayList<VarDecNode> varDecs;
    BodyNode body;

    public FuncBodyNode(ArrayList<VarDecNode> varDecs, BodyNode body) {
        this.varDecs = varDecs;
        this.body = body;
    }

    // parse
    public static FuncBodyNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            throw new SyntaxError("Empty token list for function body statement");
        }
        ArrayList<VarDecNode> varDecs = new ArrayList<>();
        // Token currentToken =;

        while (!tokens.isEmpty()
                && (tokens.get(0).getToken().equals("Boolean") || tokens.get(0).getToken().equals("Integer")
                        || tokens.get(0).getToken().equals("String") || tokens.get(0).getToken().equals("Double")
                        || tokens.get(0).getToken().equals("Void"))) {
            System.out.println("HERE: trying to parse var dec");
            System.out.println("Current Token: " + tokens.get(0).getToken());
            VarDecNode currentStmt = VarDecNode.parse(tokens);
            if (currentStmt != null) {
                varDecs.add(currentStmt);
            } else {
                break;
            }
        }

        System.out.println("HERE: parsed var decs");
        for (VarDecNode varDec : varDecs) {
            System.out.println("VarDec: " + varDec.convertToJott());
        }

        BodyNode body = BodyNode.parse(tokens);

        return new FuncBodyNode(varDecs, body);
    }

    @Override
    public String convertToJott() {
        String outStr = "";
        for (VarDecNode bodyStmt : this.varDecs) {
            String currentStmt = bodyStmt.convertToJott();
            outStr += currentStmt;
            if (currentStmt.length() > 1 && currentStmt.charAt(0) == ':' && currentStmt.charAt(1) == ':') {
                // check for function call, there might be an easier way to do this...
                outStr += ";";
            }
        }

        if (this.body != null) {
            outStr += this.body.convertToJott();
        }

        return outStr;
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

            // Test Case 1:
            ArrayList<Token> tokens2 = new ArrayList<>();
            // var dec
            tokens2.add(new Token("Boolean", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
            tokens2.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
            // body
            tokens2.add(new Token("::", "testFile.jott", 3, TokenType.FC_HEADER));
            tokens2.add(new Token("multiParamsFunc", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens2.add(new Token("[", "testFile.jott", 3, TokenType.L_BRACKET));
            tokens2.add(new Token("param1", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens2.add(new Token(",", "testFile.jott", 3, TokenType.COMMA));
            tokens2.add(new Token("param2", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens2.add(new Token(",", "testFile.jott", 3, TokenType.COMMA));
            tokens2.add(new Token("param3", "testFile.jott", 3, TokenType.ID_KEYWORD));
            tokens2.add(new Token("]", "testFile.jott", 3, TokenType.R_BRACKET));
            tokens2.add(new Token(";", "testFile.jott", 3, TokenType.SEMICOLON));
            FuncBodyNode funcBodyNode = FuncBodyNode.parse(tokens2);
            System.out.println(
                    "Parsed func body 'Boolean var; :: multiParamsFunc [param1, param2, param3];':   "
                            + funcBodyNode.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}
