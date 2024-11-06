package errors;

import provided.Token;

public class SyntaxError extends GenericError {

    // Constructor that takes both a message and a token
    public SyntaxError(String message, Token token) {
        super("Syntax Error", message, token);
    }
}