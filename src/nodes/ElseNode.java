package nodes;

import java.util.ArrayList;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Else Node
 * Else { < body >} | ε
 */
public class ElseNode implements JottTree {
    BodyNode body;
    
    public ElseNode(){
        this.body = null;
    }

    public ElseNode(BodyNode bodyNode) {
        this.body = bodyNode;
    }

    public static ElseNode parse(ArrayList<Token> tokens) throws Exception {
        if(tokens.isEmpty()) {
            return new ElseNode();
        }

        Token currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.ID_KEYWORD || !currentToken.getToken().equals("Else")) {
            throw new SyntaxError("Expected Else got " + currentToken.getToken(), currentToken);
        }
        tokens.remove(0);

        if(tokens.isEmpty()){
		    throw new SyntaxError("Else statement is missing a left brace {", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxError("Else statement missing a left brace {, instead got " + currentToken.getTokenType(), currentToken);
        }
        tokens.remove(0);

        BodyNode body = BodyNode.parse(tokens);

        if(tokens.isEmpty()){
		    throw new SyntaxError("Else if statement is missing a right brace }", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.R_BRACE) {
            throw new SyntaxError("Else if statement missing a right brace }, instead got " + currentToken.getTokenType(), currentToken);
        }
        tokens.remove(0);

        return new ElseNode(body);
    }

    @Override
    public String convertToJott() {
        if(this.body != null) {
            return "Else{ " + this.body.convertToJott() + " }";
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
