package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
import provided.Token;
import provided.TokenType;
import errors.SemanticError;

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
        return this.expr.getToken();
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
        DataType returnVal = null;

        this.expr.validateTree(symbolTable);

        if(this.expr.getType(symbolTable) != DataType.BOOLEAN) {
            throw new SemanticError("Expression in if statement must be a boolean", this.expr.getToken());
        }

        if(!this.body.validateTree(symbolTable)) {
            this.allReturn = false;
            return false;
        }

        // handle first return
        if(this.body.returnStmt != null) {
            returnVal = this.body.getReturnType();
            if(returnVal == null) {
                this.allReturn = false;
            } else {
                this.returnType = returnVal;
            }
        }

        for(ElseIfNode elseIf : this.elseIfs) {
            if(!elseIf.validateTree(symbolTable)) {
                this.allReturn = false;
                return false;
            }
            // check if each else/if block returns
            returnVal = elseIf.getReturnType();
            if(returnVal == null) {
                this.allReturn = false;
            } else {
                if(this.returnType == null) {
                    this.returnType = returnVal;
                } else if(this.returnType != returnVal) {
                    // return types don't match!
                    throw new SemanticError("Return types throughout if/elif/else block don't match", this.expr.getToken());
                }
                // else, returnType and returnVal match
            }
        }

        if(!this.elseBlock.validateTree(symbolTable)) {
            this.allReturn = false;
            return false;
        }

        returnVal = this.getReturnType();
        if(returnVal == null) {
            this.allReturn = false;
        } else {
            if(this.returnType == null) {
                this.returnType = returnVal;
            } else if(this.returnType != returnVal) {
                throw new SemanticError("Return types throughout if/elif/else block don't match", this.expr.getToken());
            }
            // else, returnType and returnVal match
        }

        return true;
    }

    @Override
    public void execute() {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }

}
