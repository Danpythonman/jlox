package com.danieldigiovanni.token;

/**
 * The type of tokens that can be expressed in Lox.
 */
public enum TokenType {

    /*
     * Single-character tokens
     */
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    COMMA,
    DOT,
    MINUS,
    PLUS,
    SEMICOLON,
    SLASH,
    STAR,

    /*
     * Boolean comparison tokens
     */
    BANG,
    BANG_EQUAL,
    EQUAL,
    EQUAL_EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,

    /*
     * Literals
     */
    STRING,
    NUMBER,

    /*
     * Language keywords
     */
    VAR,
    TRUE,
    FALSE,
    AND,
    OR,
    IF,
    ELSE,
    FOR,
    WHILE,
    CLASS,
    SUPER,
    THIS,
    FUN,
    RETURN,
    PRINT,
    NIL,

    /*
     * User-defined identifiers
     */
    IDENTIFIER,

    /*
     * End of file
     */
    EOF,

    /*
     * Unknown token
     */
    UNKNOWN

}
