package nodes;

import java.util.ArrayList;
import provided.JottTree;
import provided.Token;

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
            throw new SyntaxError("Empty token list for body statement");        
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

        // return stmt
        ReturnStmtNode returnStmt = ReturnStmtNode.parse(tokens);

        return new BodyNode(bodyStmts, returnStmt);
    }

    @Override
    public String convertToJott() {
        String outStr = "";
        for (BodyStmtNode bodyStmt : this.bodyStmts) {
            outStr += bodyStmt.convertToJott();
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
}
