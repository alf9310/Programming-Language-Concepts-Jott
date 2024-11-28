package nodes;

import errors.SemanticError;
import errors.SyntaxError;
import java.util.ArrayList;
import msc.*;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Program Node
 * < function_def >⋆ <EOF >
 */
public class ProgramNode implements JottTree{
    
    ArrayList<FuncDefNode> fDefNodes;

    public ProgramNode( ArrayList<FuncDefNode> nodes){
        this.fDefNodes = nodes;
    }

    public static ProgramNode parse(ArrayList<Token> tokens) throws Exception{
        ArrayList<FuncDefNode> nodes = new ArrayList<>();

        // Keep parsing until EOF
        try {
            while (!tokens.isEmpty()) {
                if (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken().equals("Def") || tokens
                        .get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken().equals("def")) {
                    nodes.add(FuncDefNode.parse(tokens));
                } else {
                    throw new SyntaxError("Expected function definition Def got " + tokens.get(0).getToken(), tokens.get(0));
                }
            }
            }
        catch (Exception e) {
            // PROFESSOR, Instead of passing up nulls, I catch exceptions here and return null from the program node
            System.err.println(e.getMessage());
            return null;
        }

        // The end of tokens implicitly represents EOF
        return new ProgramNode(nodes);
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    @Override
    public String convertToJott(){
        StringBuilder jottString = new StringBuilder();

        for (FuncDefNode fDefNode : fDefNodes){
            jottString.append(fDefNode.convertToJott()).append("\n");
        }
        return jottString.toString();
    }

    @Override
    public boolean validateTree(SymbolTable symbolTable) throws Exception {
        Token mainToken = null;
        for (FuncDefNode fDefNode : fDefNodes) {
            fDefNode.validateTree(symbolTable);
            if(fDefNode.funcName.getToken().getToken().equals("main")) {
                mainToken = fDefNode.funcName.getToken();
            }
        }

        if(mainToken == null) {
            if(this.fDefNodes.isEmpty()) {
                throw new SemanticError("Program does not have a main function");
            }
            Token infoToken = this.fDefNodes.get(0).funcName.getToken();
            throw new SemanticError("Program does not have a main function", infoToken);
        }

        return true;
    }

    /**
     * Set scope to main function and execute
     */
    @Override
    public Object execute(SymbolTable symbolTable) throws Exception {
        FuncDefNode mainFunction = null;

        // Get the main function
        // TODO: This could prob be stored as an attribute of program node & defined in validateTree
        for (FuncDefNode fDefNode : fDefNodes) {
            if (fDefNode.funcName.getToken().getToken().equals("main")) {
                mainFunction = fDefNode;
                break;
            }
        }
    
        // Execute the main function
        return mainFunction.execute(symbolTable);
    }

}
