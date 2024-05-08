package com.danieldigiovanni.interpreter.exception;

import com.danieldigiovanni.token.Token;

/**
 * Runtime exception representing that case where a unary operation could not
 * be evaluated due to incorrect operand type for the operator.
 */
public class LoxUnaryOperandTypeRuntimeException extends LoxRuntimeException {

    /**
     * Constructs a {@link LoxUnaryOperandTypeRuntimeException} with the token
     * of the operator that caused the exception.
     *
     * @param token The operator token that caused the exception.
     */
    public LoxUnaryOperandTypeRuntimeException(Token token) {
        super(
            token,
            String.format(
                "Unary %s operator: operand must be a number",
                token.getType()
            )
        );
    }

}
