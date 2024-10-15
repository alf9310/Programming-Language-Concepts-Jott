package nodes;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

/*
 * Additional Parameter Nodes
 * ,<expr>
 */
public class ParamsTNode implements JottTree {

    ExpressionNode expr;

    public ParamsTNode(ExpressionNode expr){   
	    this.expr = expr;
    }

    public static ParamsTNode parse(ArrayList <Token> tokens) throws Exception{

        // Check if there is tokens
        if(tokens.isEmpty()){
            throw new SyntaxError("Empty token list for ParamsT");
        }

        Token currentToken = tokens.get(0);
        // Make sure token is type COMMA ,
        if(!(currentToken.getTokenType() == TokenType.COMMA)){
            throw new SyntaxError("ParamsT does not have a comma", currentToken); 
        }
        tokens.remove(0);

        // <expr>
        ExpressionNode expr = ExpressionNode.parse(tokens);

        return new ParamsTNode(expr);
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        return "," + expr;
    }

    @Override
    public boolean validateTree() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        try {
            // Create tokens for a sample parameter list: , someExpression
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));
            tokens.add(new Token("someExpression", "testFile.jott", 1, TokenType.ID_KEYWORD));

            // Test parsing the tokens
            ParamsTNode paramsTNode = ParamsTNode.parse(tokens);

            // Output the result
            System.out.println("Parsed ParamsTNode: " + paramsTNode.convertToJott());

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}
