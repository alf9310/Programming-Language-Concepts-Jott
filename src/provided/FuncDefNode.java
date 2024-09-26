package provided;

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

        // TODO define return type & body

    }
}
