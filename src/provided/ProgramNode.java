package provided;

public class ProgramNode implements JottTree{
    
    ArrayList<FuncDefNode> fDefNodes;

    public ProgramNode( ArrayList<FuncDefNode> nodes){
        this.fDefNodes = nodes;
    }

    public static ProgramNode parse(ArrayList<Token> tokens){
        ArrayList<FuncDefNode> nodes = new ArrayList<>();

        while(!tokens.isEmpty()){
            nodes.add(FuncDefNode.parse(tokens));
        }

        return new ProgramNode(nodes);
    }

}
