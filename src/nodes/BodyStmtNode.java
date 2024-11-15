package nodes;

import errors.SyntaxError;
import java.util.ArrayList;
import msc.DataType;
import provided.JottParser;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

/*
 * Body Statement Node
 * <if_stmt> | <while_loop> | <asmt> | <func_call>;
 * since these can be followed by an optional return, this will return null when a node can't be formed
 * rather than throwing an error
 */
public interface BodyStmtNode extends JottTree {
    
    public static BodyStmtNode parse(ArrayList<Token> tokens) throws Exception {
        if(tokens.isEmpty()){
            return null;     
        }

        switch (tokens.get(0).getTokenType()) {
            case ID_KEYWORD -> {
            switch (tokens.get(0).getToken()) {
                case "If" -> {
                    return IfStmtNode.parse(tokens);
                }
                case "While" -> {
                    return WhileNode.parse(tokens);
                }
                default -> {
                    return AssignmentNode.parse(tokens);
                }
            }
                        }
            case FC_HEADER -> {
                FunctionCallNode fc = FunctionCallNode.parse(tokens);
                if(tokens.isEmpty() || tokens.get(0).getTokenType() != TokenType.SEMICOLON) {
                    throw new SyntaxError("Function call body statement missing a closing semicolon", JottParser.finalToken);
                }
                tokens.remove(0);
                return fc;
            }
            default -> {
                return null;
            }
        }
    }

    public Token getToken();

    public boolean allReturn();

    public DataType getReturnType() throws Exception;

}
