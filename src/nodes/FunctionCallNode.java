package nodes;

import provided.JottTree;
import provided.Token;
import java.util.ArrayList;

public class FunctionCallNode implements OperandNode {

    IDNode id;
    ArrayList<ParamNode> params;

    public FunctionCallNode(IdNode id, ArrayList <ParamNode> params){   
	this.id = id;
this.params = params


}

    public static FunctionCallNode parse(ArrayList <Token> tokens) throws Exception{
       if(tokens.isEmpty()){
		throw new Exception(“Syntax Error\n[message]\n[filename, linenum from this token]\n”);
}


if(!(tokens.get(0).getTokenType() == TokenType.FC_HEADER)){
            throw new Exception(“Syntax Error\n[message]\n[filename, linenum from this token]\n”);
        }
	// might want to check length first (in case no index 1 exists)
	if(!(tokens.get(1).getTokenType() == TokenType.ID_KEYWORD && 
tokens.get(1).getToken().charAt(0).isLowerCase())) {
		throw new Exception(“Syntax Error\n[message]\n[filename, linenum from this token]\n”);
}


        Token number = tokens.pop(0);
        return new FunctionCallNode(id, params);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();
        // Function id
        jottString.append(id.convertToJott()).append(" ");

        // Function Params
        for (FuncDefParam param : params) {
            jottString.append(param.convertToJott()).append(" ");
        }

        return jottString.toString();

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
