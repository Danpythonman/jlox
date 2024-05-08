package com.danieldigiovanni.interpreter.exception;

import com.danieldigiovanni.token.Token;

/**
 * Runtime exception representing that case where a binary operation could not
 * be evaluated due to incorrect operand types for the operator.
 */
public class LoxBinaryOperandsTypeRuntimeException extends LoxRuntimeException {

    /**
     * Constructs a {@link LoxBinaryOperandsTypeRuntimeException} with the
     * token of the operator that caused the exception.
     * <p>
     * Note that <b>this constructor assumes that the operator takes two
     * numeric operands</b>. This is because most binary operations in Lox
     * operate on numbers (minus, slash, star, greater than, greater than or
     * equal to, less than, less than or equal to).
     *
     * @param token The operator token that caused the exception.
     */
    public LoxBinaryOperandsTypeRuntimeException(Token token) {
        this(
            token,
            String.format(
                "Binary %s operator: operands must be both numbers",
                token.getType()
            )
        );
    }

    /**
     * Constructs a {@link LoxBinaryOperandsTypeRuntimeException} with the
     * token of the operator that caused the exception and an error message.
     * <p>
     * Note that the {@link #LoxBinaryOperandsTypeRuntimeException(Token)}
     * constructor should be preferred over this constructor to because it will
     * generate the error message based on the token automatically, thus
     * ensuring consistency in error messages.
     *
     * @param token The operator token that caused the exception.
     * @param message The error message.
     */
    protected LoxBinaryOperandsTypeRuntimeException(Token token, String message) {
        super(token, message);
    }

}
