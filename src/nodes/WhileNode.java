package nodes;

import java.util.ArrayList;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/*
 * While Node
 * While [ < expr >]{ < body >}
 */
public class WhileNode implements BodyStmtNode {
    
    ExpressionNode expr;
    BodyNode body;

    public WhileNode(ExpressionNode exprNode, BodyNode bodyNode) {
        this.expr = exprNode;
        this.body = bodyNode;
    }
    
    public static WhileNode parse(ArrayList<Token> tokens) throws Exception {

        if(tokens.isEmpty()) {
            throw new SyntaxError("Expected While got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }

        Token currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.ID_KEYWORD || !currentToken.getToken().equals("While")) {
            throw new SyntaxError("Expected While got " + currentToken.getToken(), currentToken);
        }
        tokens.remove(0);

        if(tokens.isEmpty()){
		    throw new SyntaxError("While statement is missing a left bracket [", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.L_BRACKET) {
            throw new SyntaxError("While statement missing a left bracket [, instead got " + currentToken.getTokenType(), currentToken);
        }
        tokens.remove(0);

        ExpressionNode expr = ExpressionNode.parse(tokens);

        if(tokens.isEmpty()){
		    throw new SyntaxError("While statement is missing a right bracket ]", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.R_BRACKET) {
            throw new SyntaxError("While statement missing a right bracket ], instead got " + currentToken.getTokenType(), currentToken);
        }
        tokens.remove(0);

        if(tokens.isEmpty()){
		    throw new SyntaxError("While statement is missing a left brace {", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxError("While statement missing a left brace {, instead got " + currentToken.getTokenType(), currentToken);
        }
        tokens.remove(0);

        BodyNode body = BodyNode.parse(tokens);

        if(tokens.isEmpty()){
		    throw new SyntaxError("While statement is missing a right brace }", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.R_BRACE) {
            throw new SyntaxError("While statement missing a right brace }, instead got " + currentToken.getTokenType(), currentToken);
        }
        tokens.remove(0);

        return new WhileNode(expr, body);
    }

    @Override
    public String convertToJott() {
        return "While[ " + expr.convertToJott() + " ]{ " + body.convertToJott() + " }";
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
