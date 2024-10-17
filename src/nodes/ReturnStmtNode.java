package nodes;

import java.util.ArrayList;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class ReturnStmtNode implements JottTree {
    ExpressionNode expr;

    public ReturnStmtNode() {
        this.expr = null;
    }

    public ReturnStmtNode(ExpressionNode exprNode) {
        this.expr = exprNode;
    }

    // parse
    public static ReturnStmtNode parse(ArrayList<Token> tokens) throws Exception {
        if(tokens.isEmpty()){
            return new ReturnStmtNode();     
        }

        Token currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.ID_KEYWORD || !currentToken.getToken().equals("Return")) {
            //throw new SyntaxError("Return statement does not start with the correct keyword");
            return null;
        }
        tokens.remove(0);

        ExpressionNode expr = ExpressionNode.parse(tokens);

        if(tokens.isEmpty()){
            throw new SyntaxError("Return statement missing semicolon at end");  
        }
        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.SEMICOLON) {
            throw new SyntaxError("Return statement missing semicolon at end");
        }
        tokens.remove(0);

        return new ReturnStmtNode(expr);
    }

    @Override
    public String convertToJott() {
        if(this.expr != null) {
            return "Return " + this.expr.convertToJott() + " ;";
        } else {
            return "";
        }
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
