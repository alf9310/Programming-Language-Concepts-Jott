package nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;
import java.util.ArrayList;

/*
 * Expression Node
 * <operand> | <operand> <relop> <operand> | 
 * <operand> <mathop> <operand> | <string_literal> |
 * <bool>
 */
public interface ExpressionNode extends JottTree {

    // Determine if the node is BooleanNode, StringLiteralNode, or BinaryOpNode(RelOp & MathOp). 
    // Returns the proper node type that you created above.
    public static ExpressionNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new Exception("Error, empty token list for expression"); //TODO, better errors
        }

        if(isOperand(tokens.get(0))){

            OperandNode left = OperandNode.parse(tokens);

            // < operand > < relop || mathop > < operand >
            if(tokens.get(1).getTokenType == TokenType.REL_OP || tokens.get(1).getTokenType == TokenType.MATH_OP){
                
                BinaryOpNode op = BinaryOpNode.parse(tokens);
                
                if(isOperand(tokens.get(2))){
                    OperandNode right = OperandNode.parse(tokens);
                    return BinaryOpNode(left, op, right);
                }
            }
            // < operand > 
            return left;

        // < string_literal >
        } else if(tokens.get(0).getTokenType == TokenType.STRING){
            return StringLiteralNode.parse(tokens);

        // < bool >
        } else if(tokens.get(0).getTokenType == TokenType.ID_KEYWORD){
            if(tokens.get(0).getToken().equals("True") || tokens.get(0).getToken().equals("False")){
                return BooleanNode.parse(tokens);
            }
        }

        throw new Exception("Error, invalid expression token"); //TODO, better errors
    }

    // Helper method to determine if a token is an operand (<id> | <num> | <func_call> | -<num>)
    // Return true if it is
    static boolean isOperand(Token token){
        TokenType type = token.getTokenType();
        if(type == TokenType.ID_KEYWORD){
            return true;
        } else if(type == TokenType.NUMBER){
            return true;
        } else if(type == TokenType.FC_HEADER){
            return true;
        } else if(type == TokenType.MATH_OP && token.getToken().equals("-")){
            return true;
        }
        return false;
    }
}
