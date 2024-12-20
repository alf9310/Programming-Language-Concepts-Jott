package nodes;

import java.util.ArrayList;
import msc.*;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Parameters Node
 * <expr> <params_t>⋆ | ε
 */
public class ParamsNode implements JottTree {

    ExpressionNode expr;
    ArrayList<ParamsTNode> paramst;

    public ParamsNode() {
        this.expr = null;
        this.paramst = new ArrayList<>(); // Empty parameters
    }

    public ParamsNode(ExpressionNode expr, ArrayList<ParamsTNode> paramst) {
        this.expr = expr;
        this.paramst = paramst;
    }

    public DataType getType(SymbolTable symbolTable) throws Exception {
        return this.expr.getType(symbolTable);
    }

    public static ParamsNode parse(ArrayList<Token> tokens) throws Exception {

        // Check if there is tokens
        if (tokens.isEmpty()) {
            return new ParamsNode();
        }

        // If the next token is R_BRACKET, we know there are no parameters
        // ε
        if (tokens.get(0).getTokenType() == TokenType.R_BRACKET) {
            return new ParamsNode(); // Assuming ParamsNode can handle an empty parameter list
        }

        // <expr>
        ExpressionNode expr = ExpressionNode.parse(tokens);

        // <params_t>⋆
        ArrayList<ParamsTNode> paramst = new ArrayList<>();

        Token currentToken = tokens.get(0);
        while (!tokens.isEmpty() && currentToken.getTokenType() == TokenType.COMMA) {
            paramst.add(ParamsTNode.parse(tokens));
            if (!tokens.isEmpty()) {
                currentToken = tokens.get(0);
            }
        }

        return new ParamsNode(expr, paramst);
    }

    /**
     * Will output a string of this tree in Jott
     * 
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        if (expr != null) {
            str.append(expr.convertToJott());
            for (ParamsTNode paramt : paramst) {
                str.append(paramt.convertToJott());
            }
        }
        return str.toString();
    }

    /*
     * Expression is valid
     * ParamsT is valid
     * Call to function using incorrect params (wrong number or types)
     */
    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {

        // TODO Check symbol table to make sure function is using correct param types &
        // number
        // NEED TO MAKE SURE PARAMETER EXISTS
        if (expr != null){
            expr.validateTree(symbolTable);
            for (ParamsTNode paramt : paramst) {
                paramt.validateTree(symbolTable);
            }
        }

        // Check that the number of parameters matches the function definition
        // (This is handled at the FunctionCallNode level)

        return true; // All validations passed
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        System.out.println("Testing ParamsNode Main Method");
        try {
            // Test Case 1: Empty parameter list []
            ArrayList<Token> tokens1 = new ArrayList<>();
            tokens1.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            ParamsNode paramsNode1 = ParamsNode.parse(tokens1);
            System.out.println("Parsed ParamsNode 1: " + paramsNode1.convertToJott()); // Expected output: ""

            // Test Case 2: Single parameter [param1]
            ArrayList<Token> tokens2 = new ArrayList<>();
            tokens2.add(new Token("param1", "testFile.jott", 1, TokenType.ID_KEYWORD)); // param1
            tokens2.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            ParamsNode paramsNode2 = ParamsNode.parse(tokens2);
            System.out.println("Parsed ParamsNode 2: " + paramsNode2.convertToJott()); // Expected output: "param1"

            // Test Case 3: Two parameters [param1, param2]
            ArrayList<Token> tokens3 = new ArrayList<>();
            tokens3.add(new Token("param1", "testFile.jott", 1, TokenType.ID_KEYWORD)); // param1
            tokens3.add(new Token(",", "testFile.jott", 1, TokenType.COMMA)); // ,
            tokens3.add(new Token("param2", "testFile.jott", 1, TokenType.ID_KEYWORD)); // param2
            tokens3.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            ParamsNode paramsNode3 = ParamsNode.parse(tokens3);
            System.out.println("Parsed ParamsNode 3: " + paramsNode3.convertToJott()); // Expected output: "param1,
                                                                                       // param2"

            // Test Case 4: Mixed parameter types [param1, -3, param2]
            ArrayList<Token> tokens4 = new ArrayList<>();
            tokens4.add(new Token("param1", "testFile.jott", 1, TokenType.ID_KEYWORD)); // param1
            tokens4.add(new Token(",", "testFile.jott", 1, TokenType.COMMA)); // ,
            tokens4.add(new Token("-3", "testFile.jott", 1, TokenType.NUMBER)); // -3
            tokens4.add(new Token(",", "testFile.jott", 1, TokenType.COMMA)); // ,
            tokens4.add(new Token("param2", "testFile.jott", 1, TokenType.ID_KEYWORD)); // param2
            tokens4.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            ParamsNode paramsNode4 = ParamsNode.parse(tokens4);
            System.out.println("Parsed ParamsNode 4: " + paramsNode4.convertToJott()); // Expected output: "param1, -3,
                                                                                       // param2"

            // Test Case 5: Parameters with a function call [param1, myFunc[expr1]]
            ArrayList<Token> tokens5 = new ArrayList<>();
            tokens5.add(new Token("param1", "testFile.jott", 1, TokenType.ID_KEYWORD)); // param1
            tokens5.add(new Token(",", "testFile.jott", 1, TokenType.COMMA)); // ,
            tokens5.add(new Token("::", "testFile.jott", 1, TokenType.FC_HEADER)); // ::
            tokens5.add(new Token("myFunc", "testFile.jott", 1, TokenType.ID_KEYWORD)); // myFunc
            tokens5.add(new Token("[", "testFile.jott", 1, TokenType.L_BRACKET)); // [
            tokens5.add(new Token("expr1", "testFile.jott", 1, TokenType.ID_KEYWORD)); // expr1
            tokens5.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            tokens5.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // final closing ]
            ParamsNode paramsNode5 = ParamsNode.parse(tokens5);
            System.out.println("Parsed ParamsNode 5: " + paramsNode5.convertToJott()); // Expected output: "param1,
                                                                                       // myFunc[expr1]"

            // Test Case 6: Single parameter with a negative number [-12]
            ArrayList<Token> tokens6 = new ArrayList<>();
            tokens6.add(new Token("-12", "testFile.jott", 1, TokenType.NUMBER)); // -12
            tokens6.add(new Token("]", "testFile.jott", 1, TokenType.R_BRACKET)); // ]
            ParamsNode paramsNode6 = ParamsNode.parse(tokens6);
            System.out.println("Parsed ParamsNode 6: " + paramsNode6.convertToJott()); // Expected output: "-12"

        } catch (Exception e) {
            // Catch and print any exceptions
            System.err.println("Error: " + e.getMessage());
        }
    }
}
