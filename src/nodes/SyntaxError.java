package nodes;

import provided.Token;

public class SyntaxError extends Exception {
    
    // Constructor that takes both a message and a token
    public SyntaxError(String message, Token token) {
        super(formatErrorMessage(message, token));
    }

    // Constructor that takes just a message (without a token)
    public SyntaxError(String message) {
        super("Syntax Error:\n" + message);
    }

    // Helper method to format the error message
    private static String formatErrorMessage(String message, Token token) {
        if (token != null) {
            return "Syntax Error:\n" + message + "\n" + token.getFilename() + ":" + token.getLineNum();
        } else {
            return "Syntax Error:\n" + message;
        }
    }
}