package com.danieldigiovanni.parser.exception;

import com.danieldigiovanni.token.Token;

public class LoxParseException extends RuntimeException {

    private final Token token;

    public LoxParseException(Token token, String message) {
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return this.token;
    }
}
