package nodes;

import java.util.ArrayList;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class FunctionBodyNode implements JottTree {
    private final ArrayList<JottTree> statements;  // List of statements inside the function body
    private JottTree returnStatement;        // The return statement

    public FunctionBodyNode() {
        statements = new ArrayList<>();
        returnStatement = null;
    }

    // Parse the function body from tokens
    public static FunctionBodyNode parse(ArrayList<Token> tokens) throws Exception {
        FunctionBodyNode bodyNode = new FunctionBodyNode();

        while (!tokens.isEmpty()) {
            Token token = tokens.get(0);

            // If we encounter a 'Return' token, parse the return statement
            if (token.getTokenType() == TokenType.ID_KEYWORD && token.getToken().equals("Return")) {
                ReturnStmtNode returnNode = ReturnStmtNode.parse(tokens);
                if (returnNode == null) {
                    throw new SyntaxError("Invalid return statement.");
                }
                bodyNode.returnStatement = returnNode;
                break; // Return is the last part of a function body
            }

            // Parse different types of statements
            JottTree statementNode = parseStatement(tokens);
            if (statementNode == null) {
                throw new SyntaxError("Invalid statement.");
            }
            bodyNode.statements.add(statementNode);
        }

        return bodyNode;
    }

    // Helper method to parse individual statements
    private static JottTree parseStatement(ArrayList<Token> tokens) throws Exception {
        Token token = tokens.get(0);

        // Check if it's a function call
        if (token.getTokenType() == TokenType.FC_HEADER) {
            return FunctionCallNode.parse(tokens);
        }
        
        // Check if it's an assignment
        if (token.getTokenType() == TokenType.ID_KEYWORD) {
            return AssignmentNode.parse(tokens);
        }

        throw new SyntaxError("Unexpected token: " + token.getToken());
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        for (JottTree statement : statements) {
            sb.append(statement.convertToJott()).append(" ");
        }
        if (returnStatement != null) {
            sb.append(returnStatement.convertToJott()).append(" ");
        }
        return sb.toString();
    }

    @Override
    public boolean validateTree() {
        // Validate all statements and the return statement (if any)
        for (JottTree statement : statements) {
            if (!statement.validateTree()) {
                return false;
            }
        }
        if (returnStatement != null && !returnStatement.validateTree()) {
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        // Execute all statements and the return statement (if any)
        for (JottTree statement : statements) {
            statement.execute();
        }
        if (returnStatement != null) {
            returnStatement.execute();
        }
    }
}
