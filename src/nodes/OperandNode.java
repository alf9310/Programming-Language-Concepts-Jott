package nodes;

import provided.JottTree;
import provided.Token;
import java.util.ArrayList;

public class OperandNode implements JottTree {

    Token operand;

    public OperandNode(Token operand) {
        this.operand = operand;
    }

    // Determine if the node is IDNode, number node, or FunctionCall node. Returns
    // the proper node type that you created above.
    public static OperandNode parseOperand(ArrayList <Token> tokens) throws Exception{
        if((tokens.get(0).getTokenType() == TokenType.ID_KEYWORD)){
	        return IDNode.parse(tokens);
        }
        else if((tokens.get(0).getTokenType() == TokenType.Number)){
	        return NumberNode.parse(tokens);
        }
        else if((tokens.get(0).getTokenType() == TokenType.FCHeader)){
	        return NumberNode.parse(tokens);
        }
        else {
            throw new Exception(“Syntax Error\n[message]\n[filename, linenum from this token]\n”);
        }
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return operand.getToken();
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
