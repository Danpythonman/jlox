package com.danieldigiovanni.parser.exception;

import com.danieldigiovanni.token.Token;

/**
 * Exception representing the case where all the parser rules were checked and
 * the statement being parsed did not match any of the rules.
 */
public class NoRuleFoundParseException extends LoxParseException {

    /**
     * Construct a {@link NoRuleFoundParseException} exception with the token
     * that caused the exception.
     *
     * @param token The token that caused the exception.
     */
    public NoRuleFoundParseException(Token token) {
        super(token, "No parse rule could parse the statement.");
    }

}
