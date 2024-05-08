package com.danieldigiovanni.interpreter.exception;

import com.danieldigiovanni.token.Token;
import lombok.Getter;

/**
 * Exception representing a runtime exception when executing Lox code.
 */
@Getter
public class LoxRuntimeException extends RuntimeException {

    /**
     * The token that caused the exception.
     */
    private final Token token;

    /**
     * Construct a {@link LoxRuntimeException} with the token that caused the
     * exception and an error message.
     *
     * @param token The token that caused the exception.
     * @param message The error message.
     */
    public LoxRuntimeException(Token token, String message) {
        super(message);
        this.token = token;
    }

}
