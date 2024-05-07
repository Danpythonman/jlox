package com.danieldigiovanni.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representation of a token in Lox.
 */
@Getter
@AllArgsConstructor
public class Token {

    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final int lineNumber;

    /**
     * Generate an EOF token for the last line in the source.
     *
     * @param lastLineNumber The last line number in the source.
     *
     * @return The EOF token.
     */
    public static Token eof(int lastLineNumber) {
        return new Token(TokenType.EOF, "", null, lastLineNumber);
    }

    /**
     * Generate an unknown token at the given line number.
     *
     * @param lineNumber The line number at which the unknown token was
     *                   encountered.
     *
     * @return The unknown token.
     */
    public static Token unknown(int lineNumber) {
        return new Token(TokenType.UNKNOWN, "", null, lineNumber);
    }

    @Override
    public String toString() {
        return String.format(
            "%s token: %s, %s, line %d",
            this.type,
            this.lexeme,
            this.literal,
            this.lineNumber
        );
    }

}
