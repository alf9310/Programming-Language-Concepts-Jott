package nodes;

import java.util.ArrayList;

import errors.SyntaxError;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;
import msc.*;

/*
 * Function Body Node
 * <var_dec >⋆ <body>
 */
public class FuncBodyNode implements JottTree {
    ArrayList<VarDecNode> varDecs;
    BodyNode body;
    boolean returns;
    DataType returnType;

    public FuncBodyNode(ArrayList<VarDecNode> varDecs, BodyNode body) {
        this.varDecs = varDecs;
        this.body = body;
        this.returns = false;
        this.returnType = null;
    }

    boolean allReturn() {
        return this.returns;
    }

    DataType getReturnType() {
        return this.returnType;
    }

    // parse
    public static FuncBodyNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected variable description or body got " + JottParser.finalToken.getToken(),
                    JottParser.finalToken);
        }
        ArrayList<VarDecNode> varDecs = new ArrayList<>();

        while (!tokens.isEmpty()
                && (tokens.get(0).getToken().equals("Boolean") || tokens.get(0).getToken().equals("Integer")
                        || tokens.get(0).getToken().equals("String") || tokens.get(0).getToken().equals("Double")
                        || tokens.get(0).getToken().equals("Void"))) {
            VarDecNode currentStmt = VarDecNode.parse(tokens);
            if (currentStmt != null) {
                varDecs.add(currentStmt);
            } else {
                break;
            }
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
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        // To be implemented in phase 3
        // Body can not have funcdef inside it
        for(VarDecNode varDec: this.varDecs) {
            varDec.validateTree(symbolTable);
        }

        this.body.validateTree(symbolTable);
        this.returns = this.body.allReturn();
        this.returnType = this.body.getReturnType();

        return true;
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
