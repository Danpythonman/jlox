package com.danieldigiovanni.interpreter.exception;

import com.danieldigiovanni.token.Token;

/**
 * Runtime exception representing that case where a binary operation could not
 * be evaluated because the type of the operator is unknown.
 */
public class LoxUnknownBinaryOperatorRuntimeException extends LoxRuntimeException {

    /**
     * Constructs a {@link LoxUnknownBinaryOperatorRuntimeException} with the token
     * of the operator that caused the exception.
     *
     * @param token The operator token that caused the exception.
     */
    public LoxUnknownBinaryOperatorRuntimeException(Token token) {
        super(
            token,
            String.format(
                "Unknown operator for binary expression, operator: %s",
                token.toString()
            )
        );
    }

}
