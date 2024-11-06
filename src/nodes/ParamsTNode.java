package nodes;

import java.util.ArrayList;

import errors.SyntaxError;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

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
            throw new SyntaxError("Expected comma , got " + JottParser.finalToken.getToken(), JottParser.finalToken);
        }

        Token currentToken = tokens.get(0);
        // Make sure token is type COMMA ,
        if(!(currentToken.getTokenType() == TokenType.COMMA)){
            throw new SyntaxError("Expected comma , got " + currentToken.getToken(), currentToken);
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
        return "," + expr.convertToJott();
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
        System.out.println("Testing ParamsTNode Main Method");
        try {
            // Test Case 1: Valid parameter with expression [, someExpression]
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));              // ,
            tokens1.add(new Token("someExpression", "testFile.jott", 1, TokenType.ID_KEYWORD)); // someExpression
            ParamsTNode paramsTNode1 = ParamsTNode.parse(tokens1);
            System.out.println("Parsed ParamsTNode 1: " + paramsTNode1.convertToJott());   // Expected output: ",someExpression"
    
            // Test Case 2: Valid parameter with number [, 42]
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));              // ,
            tokens2.add(new Token("42", "testFile.jott", 1, TokenType.NUMBER));            // 42
            ParamsTNode paramsTNode2 = ParamsTNode.parse(tokens2);
            System.out.println("Parsed ParamsTNode 2: " + paramsTNode2.convertToJott());   // Expected output: ",42"
    
            // Test Case 3: Valid parameter with negative number [, -99]
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));              // ,
            tokens3.add(new Token("-99", "testFile.jott", 1, TokenType.NUMBER));           // -99
            ParamsTNode paramsTNode3 = ParamsTNode.parse(tokens3);
            System.out.println("Parsed ParamsTNode 3: " + paramsTNode3.convertToJott());   // Expected output: ",-99"
    
            // Test Case 4: Invalid token (missing comma) [someExpression]
            ArrayList<Token> tokens4 = new ArrayList<>();
            tokens4.add(new Token("someExpression", "testFile.jott", 1, TokenType.ID_KEYWORD)); // someExpression
            try {
                ParamsTNode.parse(tokens4);
            } catch (Exception e) {
                System.err.println("Error in Test Case 4: " + e.getMessage());  // Expected error: ParamsT does not have a comma
            }
    
            // Test Case 5: Valid parameter with a function call [, myFunc[expr1]]
            ArrayList<Token> tokens5 = new ArrayList<>();
            tokens5.add(new Token(",", "testFile.jott", 1, TokenType.COMMA));              // ,
            tokens5.add(new Token("::", "testFile.jott", 1, TokenType.FC_HEADER));         // ::
            tokens5.add(new Token("myFunc", "testFile.jott", 1, TokenType.ID_KEYWORD));    // myFunc
            tokens5.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET));          // [
            tokens5.add(new Token("expr1", "testFile.jott", 1, TokenType.ID_KEYWORD));     // expr1
            tokens5.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET));          // ]
            ParamsTNode paramsTNode5 = ParamsTNode.parse(tokens5);
            System.out.println("Parsed ParamsTNode 5: " + paramsTNode5.convertToJott());   // Expected output: ",myFunc[expr1]"

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}
