package com.danieldigiovanni.parser.exception;

import com.danieldigiovanni.token.Token;
import lombok.Getter;

/**
 * Exception representing an exception when parsing Lox code.
 */
@Getter
public class LoxParseException extends RuntimeException {

    /**
     * The token that caused the exception.
     */
    private final Token token;

    /**
     * Construct a {@link LoxParseException} with the token that caused the
     * exception and an error message.
     *
     * @param token The token that caused the exception.
     * @param message The error message.
     */
    public LoxParseException(Token token, String message) {
        super(message);
        this.token = token;
    }

}
