package com.danieldigiovanni.interpreter.exception;

import com.danieldigiovanni.token.Token;

/**
 * Runtime exception representing that case where a unary  operation could not
 * be evaluated because the type of the operator is unknown.
 */
public class LoxUnknownUnaryOperatorRuntimeException extends LoxRuntimeException {

    /**
     * Constructs a {@link LoxUnknownUnaryOperatorRuntimeException} with the token
     * of the operator that caused the exception.
     *
     * @param token The operator token that caused the exception.
     */
    public LoxUnknownUnaryOperatorRuntimeException(Token token) {
        super(
            token,
            String.format(
                "Unknown operator for unary expression, operator: %s",
                token.toString()
            )
        );
    }

}