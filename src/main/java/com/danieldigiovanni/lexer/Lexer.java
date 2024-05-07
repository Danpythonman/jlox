package com.danieldigiovanni.lexer;

import com.danieldigiovanni.lexer.exception.LoxSyntaxException;
import com.danieldigiovanni.token.Token;
import com.danieldigiovanni.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Lexer(String source) {
        this.source = source;
    }

    /**
     * Lex all tokens of the source code.
     *
     * @return List of tokens representing the source code.
     */
    public List<Token> lexAllTokens() {
        while (this.current < this.source.length()) {
            /*
             * Set the start to current so that we can start where we left off
             * at the last token
             */
            this.start = this.current;

            /*
             * Lex the next token. This will handle grouping the correct number
             * of characters into the token, incrementing the current variable,
             * and adding the token to the list of tokens.
             */
            this.lexNextToken();
        }

        // Add a final token representing the end of file
        this.tokens.add(Token.eof(line));

        return this.tokens;
    }

    /**
     * Lex the next token.
     * <p>
     * This method handles grouping the correct number of characters into the
     * token, incrementing the current variable, and adding the token to the
     * list of tokens.
     */
    public void lexNextToken() {
        char c = this.source.charAt(current);

        switch (c) {
            // Single or double character tokens
            case '(':
            case ')':
            case '{':
            case '}':
            case ',':
            case '.':
            case '-':
            case '+':
            case ';':
            case '*':
            case '!':
            case '=':
            case '<':
            case '>':
            case '/':
                this.lexSingleOrDoubleCharacterToken();
                return;

            // Numeric literal tokens
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                this.lexNumericLiteral();
                return;

            // String literal tokens
            case '"':
                this.lexStringLiteral();
                return;

            // Whitespace
            case ' ':
            case '\r':
            case '\t':
                this.current++;
                return;

            // Newlines
            case '\n':
                this.current++;
                this.line++;
                return;

            // Everything else
            default:
                if (this.isAlphaOrUnderscore(c)) {
                    // Identifiers (both user-defined or language-defined)
                    this.lexIdentifier();
                } else {
                    // Not a valid token
                    throw new LoxSyntaxException();
                }
        }
    }

    /**
     * Lex a single or double character token.
     * <p>
     * This includes tokens like parentheses, braces, commas, plus signs,
     * double equals, less than or equals,etc. This also handles comments (both
     * single-line and multiline).
     */
    private void lexSingleOrDoubleCharacterToken() {
        TokenType tokenType = getSingleCharacterTokenType();

        if (tokenType != null) {
            // Consume the token
            this.current++;
            // Get the lexeme
            String lexeme = this.source.substring(this.start, this.current);
            // Add token to list
            this.tokens.add(new Token(tokenType, lexeme, null, this.line));
        } else {
            /*
             * In this case, the token is either a double character token or a
             * comment.
             */
            this.lexDoubleCharacterToken();
        }
    }

    /**
     * Get the token type of a single character token.
     * <p>
     * This includes tokens like parentheses, braces, commas, plus signs, etc.
     * <p>
     * If the token can possible be a double character token, this method
     * returns null.
     */
    private TokenType getSingleCharacterTokenType() {
        char c = this.source.charAt(current);
        TokenType tokenType;

        /*
         * Single character tokens.
         */
        switch (c) {
            case '(' -> tokenType = TokenType.LEFT_PAREN;
            case ')' -> tokenType = TokenType.RIGHT_PAREN;
            case '{' -> tokenType = TokenType.LEFT_BRACE;
            case '}' -> tokenType = TokenType.RIGHT_BRACE;
            case ',' -> tokenType = TokenType.COMMA;
            case '.' -> tokenType = TokenType.DOT;
            case '-' -> tokenType = TokenType.MINUS;
            case '+' -> tokenType = TokenType.PLUS;
            case ';' -> tokenType = TokenType.SEMICOLON;
            case '*' -> tokenType = TokenType.STAR;
            default -> tokenType = null;
        }

        return tokenType;
    }

    /**
     * Double character token.
     * <p>
     * This includes tokens like double equals, less than or equals, etc.
     * This also handles comments (both single-line and multiline).
     */
    private void lexDoubleCharacterToken() {
        char c = this.source.charAt(current);
        TokenType tokenType;

        switch (c) {
            case '!':
                if (this.peek() == '=') {
                    tokenType = TokenType.BANG_EQUAL;
                    this.current += 2;
                } else {
                    tokenType = TokenType.BANG;
                    this.current++;
                }
                break;
            case '=':
                if (this.peek() == '=') {
                    tokenType = TokenType.EQUAL_EQUAL;
                    this.current += 2;
                } else {
                    tokenType = TokenType.EQUAL;
                    this.current++;
                }
                break;
            case '<':
                if (this.peek() == '=') {
                    tokenType = TokenType.LESS_EQUAL;
                    this.current += 2;
                } else {
                    tokenType = TokenType.LESS;
                    this.current++;
                }
                break;
            case '>':
                if (this.peek() == '=') {
                    tokenType = TokenType.GREATER_EQUAL;
                    this.current += 2;
                } else {
                    tokenType = TokenType.GREATER;
                    this.current++;
                }
                break;
            case '/':
                /*
                 * A slash is either a slash, a single-line comment, or a
                 * multiline comment. Due to this complexity, it is in a
                 * separate method.
                 */
                this.lexSlash();
                return;
            default:
                throw new LoxSyntaxException();
        }

        // Extract lexeme
        String lexeme = this.source.substring(this.start, this.current);
        // Add token to list
        this.tokens.add(new Token(tokenType, lexeme, null, this.line));
    }

    /**
     * Lex a token that starts with a slash.
     * <p>
     * This token can either be a slash (for division), or it can be a comment
     * (either single or multiline). If it is a comment, it will not be lexed
     * into a token.
     */
    private void lexSlash() {
        char nextChar = this.peek();

        if (nextChar == '/') {
            /*
             * If the next character is also a slash, it is a single-line
             * comment.
             */

            // Consume the first slash
            this.current++;

            // Consume characters until the newline
            while (nextChar != '\n') {
                this.current++;
                nextChar = this.peek();
            }
            // Increment line number because we found a newline
            this.line++;
            // Consume the final character of the comment
            this.current++;
            // Consume the final newline
            this.current++;
        } else if (nextChar == '*') {
            /*
             * If the next character is a star, it is a multiline comment.
             */

            char nextNextChar = this.peek(2);

            // Consume characters until we see the closing "*/"
            while (!(nextChar == '*' && nextNextChar == '/')) {
                this.current++;

                // Increment the line number if we find a newline
                if (nextChar == '\n') {
                    this.line++;
                }

                nextChar = this.peek();
                nextNextChar = this.peek(2);
            }
            // Consume the final character of the comment
            this.current++;
            // Consume the "*/"
            this.current += 2;
        } else {
            // Consume the slash
            this.current++;
            // Add token to list
            this.tokens.add(new Token(TokenType.SLASH, "/", null, this.line));
        }
    }

    /**
     * Lex a string literal.
     * <p>
     * This means any text that starts with a double quotation mark. Multiline
     * strings are allowed.
     * <p>
     * If the file ends before the closing double quotation, an exception will
     * be thrown.
     */
    private void lexStringLiteral() {
        // Consume the entire string
        while (this.peek() != '"') {
            // String can be multiline
            if (this.peek() == '\n') {
                this.line++;
            }
            if (this.current >= this.source.length()) {
                // File ended before closing quotation mark, so throw error
                throw new LoxSyntaxException();
            }
            this.current++;
        }

        // Consume last character of string
        this.current++;
        // Consume closing '"'
        this.current++;
        // Extract lexeme without the opening and closing quotation marks
        String value = this.source.substring(this.start + 1, this.current - 1);
        // Add token to list
        this.tokens.add(new Token(TokenType.STRING, value, value, this.line));
    }

    /**
     * Lex a numeric literal.
     * <p>
     * This means any text that starts with a number, and optionally has a dot
     * between numbers (representing decimal points).
     */
    private void lexNumericLiteral() {
        char currentChar = this.peek();

        // Keep consuming characters until the numbers stop
        while (this.isNumeric(currentChar)) {
            this.current++;
            currentChar = this.peek();
        }

        currentChar = this.peek();
        char nextChar = this.peek(2);

        // If the current character is a dot and the number continues after it,
        // then keep lexing because the dot is just a decimal point
        if (currentChar == '.' && this.isNumeric(nextChar)) {
            // Consume the '.'
            this.current++;

            currentChar = this.peek();

            // Keep consuming characters until the numbers stop
            while (this.isNumeric(currentChar)) {
                this.current++;
                currentChar = this.peek();
            }
        }

        // Consume last character of number
        this.current++;

        // Extract lexeme
        String lexeme = this.source.substring(this.start, this.current);
        // Add token to list
        this.tokens.add(new Token(
            TokenType.NUMBER,
            lexeme,
            Double.parseDouble(lexeme), // Get the numeric value of the lexeme
            this.line
        ));
    }

    /**
     * Lex an identifier, including both user-defined and language-defined
     * identifiers.
     */
    private void lexIdentifier() {
        char currentChar = this.peek();

        // Keep consuming characters until alphanumeric values stop
        while (this.isAlphaOrUnderscore(currentChar) || this.isNumeric(currentChar)) {
            this.current++;
            currentChar = this.peek();
        }

        // Consume last character of token
        this.current++;

        // Extract lexeme
        String lexeme = this.source.substring(this.start, this.current);
        // Get type of token
        TokenType type = this.getIdentifierType(lexeme);
        // Add token to list
        this.tokens.add(new Token(type, lexeme, null, this.line));
    }

    /**
     * Get identifier type. If the identifier type does not match any
     * language-defined identifiers, then the user-defined identifier type is
     * returned.
     *
     * @param identifier The identifier being analyzed.
     *
     * @return The type of the identifier.
     */
    private TokenType getIdentifierType(String identifier) {
        return switch (identifier) {
            case "var" -> TokenType.VAR;
            case "true" -> TokenType.TRUE;
            case "false" -> TokenType.FALSE;
            case "and" -> TokenType.AND;
            case "or" -> TokenType.OR;
            case "if" -> TokenType.IF;
            case "else" -> TokenType.ELSE;
            case "for" -> TokenType.FOR;
            case "while" -> TokenType.WHILE;
            case "class" -> TokenType.CLASS;
            case "super" -> TokenType.SUPER;
            case "this" -> TokenType.THIS;
            case "fun" -> TokenType.FUN;
            case "return" -> TokenType.RETURN;
            case "print" -> TokenType.PRINT;
            case "nil" -> TokenType.NIL;
            default -> TokenType.IDENTIFIER;
        };
    }

    /**
     * Peek the next character in the source (at index {@code this.current+1}).
     * If the end of file is reached, {@code '\0'} is returned.
     *
     * @return The next character in the source.
     */
    private char peek() {
        if (this.current + 1 >= this.source.length()) {
            return '\0';
        }
        return this.source.charAt(this.current + 1);
    }

    /**
     * Peek the character in the source at index {@code lookahead} from
     * {@code this.current}. If the end of file is reached, {@code '\0'} is
     * returned.
     * <p>
     * Note that {@code peek(0)} is the character index {@code this.current}
     * and {@code peek(1)} is the same as calling {@link Lexer#peek()}.
     *
     * @param lookahead The number of indexes from the value of
     *                  {@code this.current}.
     *
     * @return The character of the source at index
     *         {@code this.current + lookahead}.
     */
    private char peek(int lookahead) {
        if (this.current + lookahead >= this.source.length()) {
            return '\0';
        }
        return this.source.charAt(this.current + lookahead);
    }

    /**
     * Checks if the given character is a letter (a-z or A-Z), or an
     * underscore.
     *
     * @param c The character to check.
     *
     * @return {@code true} if the character is a letter or an underscore,
     *         {@code false} otherwise.
     */
    private boolean isAlphaOrUnderscore(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    /**
     * Checks if the given character is a number (0-9).
     *
     * @param c The character to check.
     *
     * @return {@code true} if the character is a number {@code false}
     *         otherwise.
     */
    private boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

}
