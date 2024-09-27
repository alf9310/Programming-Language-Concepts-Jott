package nodes;

import provided.JottTree;
import provided.Token;
import java.util.ArrayList;

public class FuncDefNode implements JottTree{

    IdNode funcName;
    ArrayList<FuncDefParam> params;
    TypeNode returnType;
    FuncBodyNode body;

    public FuncDefNode(IdNode name, ArrayList<FuncDefParam> params,
                        TypeNode returnType, FuncBodyNode body){
        this.funcName = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }                    
    
    public static FuncDefNode parse(ArrayList <Token> tokens) throws Exception{
        if(!tokens.get(0).getToken().equals("Def")){
            throw Exception;
        }
        tokens.pop(0);

        IdNode name = IdNode.parse(tokens);

        if(!tokens.get(0).getToken().equals("[")){
            throw Exception;
        }
        tokens.pop(0);

        ArrayList<FuncDefParam> params = FuncDefParam.parse(tokens);

        if(!tokens.get(0).getToken().equals("]")){
            throw Exception;
        }
        tokens.pop(0);

        if(!tokens.get(0).getToken().equals(":")){
            throw Exception;
        }
        tokens.pop(0);

        TypeNode returnType = TypeNode.parse(tokens);

        if(!tokens.get(0).getToken().equals("{")){
            throw Exception;
        }
        tokens.pop(0);

        FuncBodyNode body = FuncBodyNode.parse(tokens);

        if(!tokens.get(0).getToken().equals("}")){
            throw Exception;
        }
        tokens.pop(0);

        return new FuncDefNode(name, params, returnType, body);
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott(){
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
