package nodes;

import provided.JottTree;
import provided.Token;
import java.util.ArrayList;

public class IDNode implements OperandNode {

    Token id;

    public IDNode(Token id) {
        this.id = id;

    }

    public static IDNode parse(ArrayList <Token> tokens) throws Exception{
       if(tokens.isEmpty()){
		throw new Exception(“Syntax Error\n[message]\n[filename, linenum from this token]\n”);
}


if(!(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD)){
            throw new Exception(“Syntax Error\n[message]\n[filename, linenum from this token]\n”);
        }
	else if(tokens.get(0).getToken().charAt(0).isUpperCase()) {
		throw new Exception(“Syntax Error\n[msg]\n[file,line]\n”);
}


        Token id = tokens.pop(0);
        return new IDNode(id);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return id.getToken();
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
