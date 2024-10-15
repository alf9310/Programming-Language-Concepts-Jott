package nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

/*
 * Parameters Node
 * <expr> <params_t>⋆ | ε
 */
public class ParamsNode implements JottTree {

    ExpressionNode expr;
    ArrayList<ParamsTNode> paramst;

    public ParamsNode(ExpressionNode expr, ArrayList<ParamsTNode> paramst){   
	    this.expr = expr;
        this.paramst = paramst;
    }

    public static ParamsNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            // ε
		    return new ParamsNode(null, null);
        }

        // <expr>
        ExpressionNode expr = ExpressionNode.parse(tokens);

        // <params_t>⋆
        ArrayList<ParamsTNode> paramst = new ArrayList<>();
        
        // TODO verify this works as intended
        Token currentToken = tokens.get(0);
        while(isOperand(currentToken)){
            paramst.add(ParamsTNode.parse(tokens));
            currentToken = tokens.get(0);
        }

        return new ParamsNode(expr, paramst);
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

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        String str = "";
        if(expr != null){
            str += expr.convertToJott();
            for(ParamsTNode paramt: paramst){
                str += paramt.convertToJott();
            }
        }
        return str;
    }

    @Override
    public boolean validateTree() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
