package nodes;

import java.util.ArrayList;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Body Node
 * Can either be a boolean equation or a mathmatical equation
 * < body_stmt >⋆ < return_stmt >
 */
public class BodyNode implements JottTree {
    ArrayList<BodyStmtNode> bodyStmts;
    ReturnStmtNode returnStmt;

    public BodyNode(ArrayList<BodyStmtNode> bodyStmtNodes, ReturnStmtNode returnStmtNode) {
        this.bodyStmts = bodyStmtNodes;
        this.returnStmt = returnStmtNode;
    }

    // parse
    public static BodyNode parse(ArrayList<Token> tokens) throws Exception {
        if(tokens.isEmpty()){
            throw new SyntaxError("Expected return statement got " + JottParser.finalToken.getToken(), JottParser.finalToken);            
        }

        ArrayList<BodyStmtNode> bodyStmts = new ArrayList<>();
        while(!tokens.isEmpty() && !tokens.get(0).getToken().equals("Return")) {
            BodyStmtNode currentStmt = BodyStmtNode.parse(tokens);
            if(currentStmt != null) {
                bodyStmts.add(currentStmt);
            } else {
                break;
            }
        }

        ReturnStmtNode returnStmt = ReturnStmtNode.parse(tokens);

        return new BodyNode(bodyStmts, returnStmt);
    }

    @Override
    public String convertToJott() {
        String outStr = "";
        for (BodyStmtNode bodyStmt : this.bodyStmts) {
            String currentStmt = bodyStmt.convertToJott();
            outStr += currentStmt;
            if(currentStmt.length() > 1 && currentStmt.charAt(0) == ':' && currentStmt.charAt(1) == ':') {
                // check for function call, there might be an easier way to do this... 
                outStr += ";";
            }
        }

        if(this.returnStmt != null) {
            outStr += this.returnStmt.convertToJott();
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
        System.out.println("testing body node main method");
        ArrayList<Token> tokens1 = new ArrayList<>();
        tokens1.add(new Token("If", "filename", 1, TokenType.ID_KEYWORD));
        tokens1.add(new Token("[", "filename", 1, TokenType.L_BRACKET));
        tokens1.add(new Token("5", "filename", 1, TokenType.NUMBER));
        tokens1.add(new Token("]", "filename", 1, TokenType.R_BRACKET));
        tokens1.add(new Token("{", "filename", 1, TokenType.L_BRACE));
        tokens1.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
        tokens1.add(new Token("=", "testFile.jott", 1, TokenType.ASSIGN));
        tokens1.add(new Token("4", "testFile.jott", 1, TokenType.NUMBER));
        tokens1.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
        tokens1.add(new Token("}", "filename", 1, TokenType.R_BRACE));
        tokens1.add(new Token("Elseif", "filename", 1, TokenType.ID_KEYWORD));
        tokens1.add(new Token("[", "filename", 1, TokenType.L_BRACKET));
        tokens1.add(new Token("0", "filename", 1, TokenType.NUMBER));
        tokens1.add(new Token("]", "filename", 1, TokenType.R_BRACKET));
        tokens1.add(new Token("{", "filename", 1, TokenType.L_BRACE));
        tokens1.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
        tokens1.add(new Token("=", "testFile.jott", 1, TokenType.ASSIGN));
        tokens1.add(new Token("3", "testFile.jott", 1, TokenType.NUMBER));
        tokens1.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
        tokens1.add(new Token("}", "filename", 1, TokenType.R_BRACE));
        tokens1.add(new Token("Else", "filename", 1, TokenType.ID_KEYWORD));
        tokens1.add(new Token("{", "filename", 1, TokenType.L_BRACE));
        tokens1.add(new Token("var", "testFile.jott", 1, TokenType.ID_KEYWORD));
        tokens1.add(new Token("=", "testFile.jott", 1, TokenType.ASSIGN));
        tokens1.add(new Token("2", "testFile.jott", 1, TokenType.NUMBER));
        tokens1.add(new Token(";", "testFile.jott", 1, TokenType.SEMICOLON));
        tokens1.add(new Token("}", "filename", 1, TokenType.R_BRACE));
        BodyNode BodyNode1 = BodyNode.parse(tokens1);
        System.out.println("Parsed BodyNode: " + BodyNode1.convertToJott());
        ArrayList<Token> tokens2 = new ArrayList<>();
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
        BodyNode BodyNode2 = BodyNode.parse(tokens2);
        System.out.println("Parsed BodyNode2: " + BodyNode2.convertToJott());
        ArrayList<Token> tokens3 = new ArrayList<>();
        tokens3.add(new Token("While", "tester", 5, TokenType.ID_KEYWORD));
        tokens3.add(new Token("[", "tester", 5, TokenType.L_BRACKET));
        tokens3.add(new Token("3", "tester", 5, TokenType.NUMBER));
        tokens3.add(new Token("]", "tester", 5, TokenType.R_BRACKET));
        tokens3.add(new Token("{", "tester", 5, TokenType.L_BRACE));
        tokens3.add(new Token("var", "tester", 5, TokenType.ID_KEYWORD));
        tokens3.add(new Token("=", "tester", 5, TokenType.ASSIGN));
        tokens3.add(new Token("3", "tester", 5, TokenType.NUMBER));
        tokens3.add(new Token(";", "tester", 5, TokenType.SEMICOLON));
        tokens3.add(new Token("}", "tester", 5, TokenType.R_BRACE));
        BodyNode BodyNode3 = BodyNode.parse(tokens3);
        System.out.println("Parsed BodyNode3: " + BodyNode3.convertToJott());

        ArrayList<Token> errTokens = new ArrayList<>();
        errTokens.add(new Token("::", "testFile.jott", 3, TokenType.FC_HEADER));
        errTokens.add(new Token("multiParamsFunc", "testFile.jott", 3, TokenType.ID_KEYWORD));
        errTokens.add(new Token("[", "testFile.jott", 3, TokenType.L_BRACKET));
        errTokens.add(new Token("param1", "testFile.jott", 3, TokenType.ID_KEYWORD));
        errTokens.add(new Token(",", "testFile.jott", 3, TokenType.COMMA));
        errTokens.add(new Token("param2", "testFile.jott", 3, TokenType.ID_KEYWORD));
        errTokens.add(new Token(",", "testFile.jott", 3, TokenType.COMMA));
        errTokens.add(new Token("param3", "testFile.jott", 3, TokenType.ID_KEYWORD));
        errTokens.add(new Token("]", "testFile.jott", 3, TokenType.R_BRACKET));
        BodyNode BodyNodeErr = BodyNode.parse(errTokens);
        System.out.println("Parsed BodyNodeErr: " + BodyNodeErr.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}
