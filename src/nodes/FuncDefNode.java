// TODO: Needs major overhaul, updated Syntax Errors, correct Node types, comments, etc.
package nodes;

import provided.JottTree;
import provided.Token;
import java.util.ArrayList;
import provided.TokenType;

public class FuncDefNode implements JottTree{

    IDNode funcName;
    ArrayList<FuncDefParam> params;
    TypeNode returnType;
    FuncBodyNode body;

    public FuncDefNode(IDNode name, ArrayList<FuncDefParam> params,
                        TypeNode returnType, FuncBodyNode body){
        this.funcName = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }                    
    
    public static FuncDefNode parse(ArrayList <Token> tokens) throws Exception{
        if(!tokens.get(0).getToken().equals("Def")){
            throw new SyntaxError("Function definition does not start with the correct keyword");
        }
        tokens.remove(0);

        IDNode name = IDNode.parse(tokens);

        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET){
            throw new SyntaxError("Function definition missing left bracket");
        }
        tokens.remove(0);

        ArrayList<FuncDefParam> params = FuncDefParam.parse(tokens);

        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET){
            throw new SyntaxError("Function definition missing right bracket");
        }
        tokens.remove(0);

        if(tokens.get(0).getTokenType() != TokenType.COLON){
            throw new SyntaxError("Function definition missing colon");
        }
        tokens.remove(0);

        TypeNode returnType = TypeNode.parse(tokens);

        if(tokens.get(0).getTokenType() != TokenType.L_BRACE){
            throw new SyntaxError("Function definition missing left brace");
        }
        tokens.remove(0);

        FuncBodyNode body = FuncBodyNode.parse(tokens);

        if(tokens.get(0).getTokenType() != TokenType.R_BRACE){
            throw new SyntaxError("Function definition missing right brace");
        }
        tokens.remove(0);

        return new FuncDefNode(name, params, returnType, body);
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott(){
        // need to add "Def" to beginning?
        
        StringBuilder jottString = new StringBuilder();
        // Function Name
        jottString.append(funcName.convertToJott()).append(" ");

        // Function Params
        for (FuncDefParam param : params){
            jottString.append(param.convertToJott()).append(" ");
        }
        // Return Type
        jottString.append(returnType.convertToJott()).append(" ");

        // Body
        jottString.append(body.convertToJott()).append(" ");

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
