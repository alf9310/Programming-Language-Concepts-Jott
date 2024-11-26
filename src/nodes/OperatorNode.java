package nodes;

import msc.*;
import provided.Token;
import provided.TokenType;

/**
 * Interface for RelOpNode and MathOpNode
 * Only exists so BinaryOpNode can have a generic operation node
 */
public interface OperatorNode extends ExpressionNode {
    @Override
    String convertToJott();

    TokenType getTokenType();

    Token getToken();

    @Override
    boolean validateTree(SymbolTable symbolTable);
    
    @Override
    Object execute(SymbolTable symbolTable);
}