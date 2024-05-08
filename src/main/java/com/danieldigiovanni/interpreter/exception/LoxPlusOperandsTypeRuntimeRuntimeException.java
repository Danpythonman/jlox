package com.danieldigiovanni.interpreter.exception;

import com.danieldigiovanni.token.Token;
import com.danieldigiovanni.token.TokenType;

/**
 * Runtime exception representing that case where a plus operation could not be
 * evaluated due to incorrect operand types for the plus operator. This is a
 * special case of the {@link LoxBinaryOperandsTypeRuntimeException} and must
 * be handled differently because the plus operation can be performed on both
 * numbers and strings, whereas the
 * {@link LoxBinaryOperandsTypeRuntimeException} assumes that the operator
 * operates on numbers only.
 */
public class LoxPlusOperandsTypeRuntimeRuntimeException extends LoxBinaryOperandsTypeRuntimeException {

    /**
     * Constructs a {@link LoxPlusOperandsTypeRuntimeRuntimeException} with the
     * token of the operator that caused the exception.
     *
     * @param token The operator token that caused the exception.
     */
    public LoxPlusOperandsTypeRuntimeRuntimeException(Token token) {
        super(
            token,
            String.format(
                "Binary %s operator: operands must be both numbers or both "
                    + "strings",
                TokenType.PLUS
            )
        );
    }

}
