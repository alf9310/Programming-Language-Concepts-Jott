package errors;

import provided.Token;

/*
 * Runtime Error
 * Thrown when encountering an error during execution (like division by 0)
 */
public class RuntimeError extends GenericError {

    // Constructor that takes both a message and a token
    public RuntimeError(String message, Token token) {
        super("Runtime Error", message, token);
    }

    // Constructor that takes just a message
    public RuntimeError(String message) {
        super("Runtime Error", message);
    }
}