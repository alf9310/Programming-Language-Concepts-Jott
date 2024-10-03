package nodes;

import provided.JottTree;
import provided.Token;
import java.util.ArrayList;

public class NumberNode implements OperandNode {

    Token number;

    public NumberNode(Token number) {
        this.number = number;

    }

    public static NumberNode parse(ArrayList <Token> tokens) throws Exception{
       if(tokens.isEmpty()){
		throw new Exception(“Syntax Error\n[message]\n[filename, linenum from this token]\n”);
}


if(!(tokens.get(0).getTokenType() == TokenType.NUMBER)){
            throw new Exception(“Syntax Error\n[message]\n[filename, linenum from this token]\n”);
        }


        Token number = tokens.pop(0);
        return new NumberNode(number);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return number.getToken();
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
