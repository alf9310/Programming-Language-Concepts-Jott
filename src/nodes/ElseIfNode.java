package nodes;

import java.util.ArrayList;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class ElseIfNode implements JottTree {
    // vars
    ExpressionNode expr;
    BodyNode body;

    // constructor
    public ElseIfNode(ExpressionNode exprNode, BodyNode bodyNode) {
        this.expr = exprNode;
        this.body = bodyNode;
    }

    // parse
    public static ElseIfNode parse(ArrayList<Token> tokens) throws Exception {
        if(tokens.isEmpty()) {
            throw new SyntaxError("Empty token list for else if");
        }

        Token currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.ID_KEYWORD || !currentToken.getToken().equals("Elseif")) {
            throw new SyntaxError("Else if statement does not start with correct keyword");
        }
        tokens.remove(0);

        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.L_BRACKET) {
            throw new SyntaxError("Else if statement missing a left bracket");
        }
        tokens.remove(0);

        ExpressionNode expr = ExpressionNode.parse(tokens);

        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.R_BRACKET) {
            throw new SyntaxError("Else if statement missing a right bracket");
        }
        tokens.remove(0);

        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxError("Else if statement missing a left brace");
        }
        tokens.remove(0);

        BodyNode body = BodyNode.parse(tokens);

        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.R_BRACE) {
            throw new SyntaxError("Else if statement missing a right brace");
        }
        tokens.remove(0);

        return new ElseIfNode(expr, body);
    }

    @Override
    public String convertToJott() {
        String outStr = "Elseif[ " + this.expr.convertToJott() + " ]{ " + this.body.convertToJott() + " }";
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
