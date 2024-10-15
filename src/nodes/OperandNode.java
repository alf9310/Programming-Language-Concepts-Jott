package nodes;

import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

/*
 * Operand Node
 * <id> | <num> | <func_call> | -<num>
 */
public interface OperandNode extends ExpressionNode {

    // Determine if the node is IDNode, (-)NumberNode, or FunctionCallNode. 
    // Returns the proper node type that you created above.
    public static OperandNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("Empty token list for operand");        
        }

        Token currentToken = tokens.get(0);

        // <id>
        if(currentToken.getTokenType() == TokenType.ID_KEYWORD){
	        return IDNode.parse(tokens);
        // <num>
        } else if(currentToken.getTokenType() == TokenType.NUMBER){
	        return NumberNode.parse(tokens);
        // <func_call>
        } else if(currentToken.getTokenType() == TokenType.FC_HEADER){
	        return FunctionCallNode.parse(tokens);
        // -<num>
        } else if(currentToken.getToken().equals("-")){
            if(currentToken.getTokenType() == TokenType.NUMBER){
                tokens.remove(0);
                return NumberNode.parse(tokens); // TODO Find some way to negate this number for the NumberNode
            }
        }

        throw new SyntaxError("TokenType is not ID_KEYWORD, (-)NUMBER or FC_HEADER for Operand", currentToken);
    }
}
