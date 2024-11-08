package errors;

import provided.Token;

/*
 * GenericError Class
 * Generic for formatting Syntax and Semantic Errors
 */
public abstract class GenericError extends Exception{
    public GenericError(String errorType, String message, Token token){
        super("\n" + errorType + ":\n" + message + "\n" + token.getFilename() + ":" + token.getLineNum());
    }

}
