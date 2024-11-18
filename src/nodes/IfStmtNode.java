package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
import provided.Token;
import provided.TokenType;

/*
 * If Statement Node
 * If[<expr>]{<body>}<elseif_lst>*<else>
 */
public class IfStmtNode implements BodyStmtNode {

    ExpressionNode expr;
    BodyNode body;
    ArrayList<ElseIfNode> elseIfs;
    ElseNode elseBlock;
    boolean allReturn;      // use to check if if/elif/else is "returnable" or if we need another return outside of this
    DataType returnType;    // use to store type of value being returned by all if/elif/else

    public IfStmtNode(ExpressionNode exprNode, BodyNode bodyNode, ArrayList<ElseIfNode> elseIfNodes,
            ElseNode elseNode) {
        this.expr = exprNode;
        this.body = bodyNode;
        this.elseIfs = elseIfNodes;
        this.elseBlock = elseNode;
        this.allReturn = false;
        this.returnType = null;
    }

    @Override
    public boolean allReturn() {
        return this.allReturn;
    }

    @Override
    public DataType getReturnType() {
        return this.returnType;
    }

    public static IfStmtNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected If got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }

        Token currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.ID_KEYWORD || !currentToken.getToken().equals("If")) {
            throw new SyntaxError("Expected If got " + currentToken.getToken(), currentToken);
        }
        tokens.remove(0);

        if (tokens.isEmpty()) {
            throw new SyntaxError("If statement is missing a left bracket [", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if(currentToken.getTokenType() == TokenType.ASSIGN) {
            throw new SemanticError("Cannot assign If a value", currentToken);
        }
        if (currentToken.getTokenType() != TokenType.L_BRACKET) {
            throw new SyntaxError("If statement missing a left bracket [, instead got " + currentToken.getTokenType(),
                    currentToken);
        }
        tokens.remove(0);

        ExpressionNode expr = ExpressionNode.parse(tokens); // error checking handled

        if (tokens.isEmpty()) {
            throw new SyntaxError("If statement is missing a right bracket ]", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.R_BRACKET) {
            throw new SyntaxError("If statement missing a right bracket ], instead got " + currentToken.getTokenType(),
                    currentToken);
        }
        tokens.remove(0);

        if (tokens.isEmpty()) {
            throw new SyntaxError("If statement is missing a left brace {", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxError("If statement missing a left brace {, instead got " + currentToken.getTokenType(),
                    currentToken);
        }
        tokens.remove(0);

        // body
        BodyNode body = BodyNode.parse(tokens);

        // brace
        if (tokens.isEmpty()) {
            throw new SyntaxError("If statement is missing a right brace }", JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.R_BRACE) {
            throw new SyntaxError("If statement missing a right brace }, instead got " + currentToken.getTokenType(),
                    currentToken);
        }
        tokens.remove(0);

        // else ifs
        ArrayList<ElseIfNode> elseIfNodes = new ArrayList<>();
        while (!tokens.isEmpty() && tokens.get(0).getToken().equals("Elseif")) {
            elseIfNodes.add(ElseIfNode.parse(tokens));
        }

        // else
        ElseNode elseNode = ElseNode.parse(tokens);

        return new IfStmtNode(expr, body, elseIfNodes, elseNode); // edit later
    }

    @Override
    public Token getToken() {
        if(this.body.getToken() != null) {
            return this.body.getToken();
        } else {
            return this.expr.getToken();
        }
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
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        // To be implemented in phase 3
        this.allReturn = true;

        this.expr.validateTree(symbolTable);
        if(this.expr.getType(symbolTable) != DataType.BOOLEAN) {
            throw new SemanticError("Expression in if statement must be a boolean", this.expr.getToken());
        }

        // Check returns
        this.body.validateTree(symbolTable);
        if(!this.body.allReturn()) {
            this.allReturn = false;
        }
        for(ElseIfNode elseIf: this.elseIfs) {
            elseIf.validateTree(symbolTable);
            if(!elseIf.allReturn()) {
                this.allReturn = false;
            }
        }
        if (this.elseBlock.body != null) {
            this.elseBlock.validateTree(symbolTable);
            if(!this.elseBlock.allReturn()) {
                this.allReturn = false;
            }
        } else {
            this.allReturn = false;
        }

        return true;
    }

    @Override
    public void execute() {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }

}
