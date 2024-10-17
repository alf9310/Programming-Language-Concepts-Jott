package nodes;

import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

/*
 * If[<expr>]{<body>}<elseif_lst>*<else>
 */
public class IfStmtNode implements BodyStmtNode {
    
    ExpressionNode expr;
    BodyNode body;
    ArrayList<ElseIfNode> elseIfs;
    ElseNode elseBlock;

    public IfStmtNode(ExpressionNode exprNode, BodyNode bodyNode, ArrayList<ElseIfNode> elseIfNodes, ElseNode elseNode) {
        this.expr = exprNode;
        this.body = bodyNode;
        this.elseIfs = elseIfNodes;
        this.elseBlock = elseNode;
    }

    public static IfStmtNode parse(ArrayList<Token> tokens) throws Exception {
        if(tokens.isEmpty()){
		    throw new SyntaxError("Empty token list for if statement"); 
        }

        Token currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.ID_KEYWORD || !currentToken.getToken().equals("If")) {
            throw new SyntaxError("If statement does not start with correct keyword");
        }
        tokens.remove(0);

        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.L_BRACKET) {
            throw new SyntaxError("If statement is missing a left bracket");
        }
        tokens.remove(0);

        ExpressionNode expr = ExpressionNode.parse(tokens); // error checking handled

        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.R_BRACKET) {
            throw new SyntaxError("If statement is missing a right bracket");
        }
        tokens.remove(0);

        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxError("If statement is missing a left brace");
        }
        tokens.remove(0);

        // body
        BodyNode body = BodyNode.parse(tokens);

        // brace
        currentToken = tokens.get(0);
        if(currentToken.getTokenType() != TokenType.R_BRACE) {
            throw new SyntaxError("If statement is missing a right brace");
        }
        tokens.remove(0);

        // else ifs
        ArrayList<ElseIfNode> elseIfNodes = new ArrayList<>();
        while(!tokens.isEmpty() && tokens.get(0).getToken().equals("Elseif")) {
            elseIfNodes.add(ElseIfNode.parse(tokens));
        }

        // else
        ElseNode elseNode = ElseNode.parse(tokens);        
        
        return new IfStmtNode(expr, body, elseIfNodes, elseNode);    // edit later
    }

    @Override
    public String convertToJott() {
        String outStr = "If[ " + this.expr.convertToJott() + " ]{ " + this.body.convertToJott() + " }";
        for (ElseIfNode elseIf : this.elseIfs) {
            outStr += elseIf.convertToJott();
        }
        outStr += elseBlock.convertToJott();
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
