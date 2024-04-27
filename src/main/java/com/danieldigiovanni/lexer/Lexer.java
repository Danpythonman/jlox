package com.danieldigiovanni.lexer;

import com.danieldigiovanni.lexer.exception.LoxSyntaxException;
import com.danieldigiovanni.token.Token;
import com.danieldigiovanni.token.TokenType;
import lombok.AllArgsConstructor;

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

    public List<Token> lexAllTokens() {
        while (this.current < this.source.length()) {
            this.start = this.current;
            this.lexNextToken();
        }

        this.tokens.add(Token.eof(line));

        return this.tokens;
    }

    public void lexNextToken() {
        char c = this.source.charAt(current);
        this.current++;

        TokenType tokenType;
        String lexeme = this.source.substring(this.start, this.current);

        switch (c) {
            case '(':
                tokenType = TokenType.LEFT_PAREN;
                break;
            case ')':
                tokenType = TokenType.RIGHT_PAREN;
                break;
            case '{':
                tokenType = TokenType.LEFT_BRACE;
                break;
            case '}':
                tokenType = TokenType.RIGHT_BRACE;
                break;
            case ',':
                tokenType = TokenType.COMMA;
                break;
            case '.':
                tokenType = TokenType.DOT;
                break;
            case '-':
                tokenType = TokenType.MINUS;
                break;
            case '+':
                tokenType = TokenType.PLUS;
                break;
            case ';':
                tokenType = TokenType.SEMICOLON;
                break;
            case '*':
                tokenType = TokenType.STAR;
                break;
            case '!':
                if (this.peek() == '=') {
                    tokenType = TokenType.BANG_EQUAL;
                    this.current++;
                } else {
                    tokenType = TokenType.BANG;
                }
                break;
            case '=':
                if (this.peek() == '=') {
                    tokenType = TokenType.EQUAL_EQUAL;
                    this.current++;
                } else {
                    tokenType = TokenType.EQUAL;
                }
                break;
            case '<':
                if (this.peek() == '=') {
                    tokenType = TokenType.LESS_EQUAL;
                    this.current++;
                } else {
                    tokenType = TokenType.LESS;
                }
                break;
            case '>':
                if (this.peek() == '=') {
                    tokenType = TokenType.GREATER_EQUAL;
                    this.current++;
                } else {
                    tokenType = TokenType.GREATER;
                }
                break;
            case '/':
                if (this.peek() == '/') {
                    while (this.peek() != '\n') {
                        this.current++;
                    }
                    return;
                } else {
                    tokenType = TokenType.SLASH;
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                return;
            case '\n':
                this.line++;
                return;
            default:
                throw new LoxSyntaxException();
        }

        this.tokens.add(new Token(tokenType, lexeme, null, this.line));
    }

    private char peek() {
        if (this.current + 1 >= this.source.length()) {
            return '\0';
        }
        return this.source.charAt(this.current + 1);
    }

    private void lexStringLiteral() {
        while (this.peek() != '"') {
            if (this.peek() == '\n') {
                this.line++;
            }
            if (this.current >= this.source.length()) {
                throw new LoxSyntaxException();
            }
            this.current++;
        }

        this.current++;

        String value = this.source.substring(this.start + 1, this.current - 1);

        this.tokens.add(new Token(TokenType.STRING, value, null, this.line));
    }

    private boolean matchNextCharacter(char expectedCharacter) {
        if (this.current >= this.source.length()) {
            return false;
        }
        if (this.source.charAt(this.current) == expectedCharacter) {
            this.current++;
            return true;
        }
        return false;
    }

}
