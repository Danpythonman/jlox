package com.danieldigiovanni.parser.exception;

import com.danieldigiovanni.token.Token;

/**
 * Exception representing the case where the parser tried to parse an
 * expression surrounded by parentheses, but could not find the right (closing)
 * parenthesis.
 */
public class MissingRightParenParseException extends LoxParseException {

    /**
     * Construct a {@link MissingRightParenParseException} exception with the
     * token that caused the exception.
     *
     * @param token The token that caused the exception.
     */
    public MissingRightParenParseException(Token token) {
        super(
            token,
            "The right (closing) parenthesis could not be found when parsing "
                + "an expression surrounded by parentheses as part of the "
                + "primary rule."
        );
    }
}
