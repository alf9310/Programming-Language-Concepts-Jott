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
}