package errors;

import provided.Token;

/*
 * Semantic Error
 * Thrown when encountering an error with validation
 */
public class SemanticError extends GenericError {

    // Constructor that takes both a message and a token
    public SemanticError(String message, Token token) {
        super("Semantic Error", message, token);
    }

    // Constructor that takes just a message
    public SemanticError(String message) {
        super("Semantic Error", message);
    }
}