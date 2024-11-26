package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Return Statement Node
 * Return < expr >; | ε
 */
public class ReturnStmtNode implements JottTree {
    ExpressionNode expr;
    DataType returnType;

    public ReturnStmtNode() {
        this.expr = null;
        this.returnType = null;
    }

    public ReturnStmtNode(ExpressionNode exprNode) {
        this.expr = exprNode;
        this.returnType = null;
    }

    public DataType getReturnType() {
        return this.returnType;
    }

    public Token getToken() {
        if(this.expr == null) {
            return null;
        }
        return this.expr.getToken();
    }

    // parse
    public static ReturnStmtNode parse(ArrayList<Token> tokens) throws Exception {
        if (tokens.isEmpty()) {
            return new ReturnStmtNode();
        }

        Token currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.ID_KEYWORD || !currentToken.getToken().equals("Return")) {
            return null;
        }
        tokens.remove(0);

        ExpressionNode expr = ExpressionNode.parse(tokens);

        if (tokens.isEmpty()) {
            throw new SyntaxError("Expected semicolon ; after return got " + JottParser.finalToken.getToken(),
                    JottParser.finalToken);
        }
        currentToken = tokens.get(0);
        if (currentToken.getTokenType() != TokenType.SEMICOLON) {
            throw new SyntaxError("Expected semicolon ; after return got " + currentToken.getToken(), currentToken);
        }
        tokens.remove(0);

        return new ReturnStmtNode(expr);
    }

    @Override
    public String convertToJott() {
        if (this.expr != null) {
            return "Return " + this.expr.convertToJott() + " ;";
        } else {
            return "";
        }
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        // Return type matches return statement
        if(this.expr == null) {
            // body w/o return is valid
            return true;
        } else {
            this.expr.validateTree(symbolTable);

            this.returnType = this.expr.getType(symbolTable);

            String func = symbolTable.current_scope;
            FunctionInfo info = symbolTable.getFunction(func);
            DataType funcReturn = info.getReturnDataType();
            if(funcReturn == null) {
                throw new SemanticError("Function return should be one of the data types provided or VOID", this.expr.getToken());
            }

            if(this.returnType == null || this.returnType == DataType.VOID) {
                // should not return null/VOID
                throw new SemanticError("Functions cannot return VOID", this.expr.getToken());
            } else if(funcReturn == DataType.VOID) {
                // function shouldn't return
                throw new SemanticError("Function with VOID return type shouldn't return", this.expr.getToken());
            } else if(funcReturn != this.returnType) {
                throw new SemanticError(func + " should return type " + funcReturn + ", returns " + this.returnType + " instead", this.expr.getToken());
            }
            return true;
        }
    }

    @Override
    public void execute(SymbolTable symbolTable) {
        // To be implemented in phase 4
        throw new UnsupportedOperationException("Execution not supported yet.");
    }
}
