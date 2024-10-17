package nodes;

import java.util.ArrayList;
import provided.JottTree;
import provided.Token;

/*
 * <if_stmt> | <while_loop> | <asmt> | <func_call>;
 * since these can be followed by an optional return, this will return null when a node can't be formed
 * rather than throwing an error
 */
public interface BodyStmtNode extends JottTree {
    
    public static BodyStmtNode parse(ArrayList<Token> tokens) throws Exception {
        if(tokens.isEmpty()){
            //throw new SyntaxError("Empty token list for body statement");   
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
                return FunctionCallNode.parse(tokens);
            }
            default -> {
                return null;//throw new SyntaxError("Invalid start of body statement");
            }
        }
    }

}
