package com.danieldigiovanni.parser;

import com.danieldigiovanni.expr.Binary;
import com.danieldigiovanni.expr.Expr;
import com.danieldigiovanni.expr.Grouping;
import com.danieldigiovanni.expr.Literal;
import com.danieldigiovanni.expr.Unary;
import com.danieldigiovanni.parser.exception.LoxParseException;
import com.danieldigiovanni.token.Token;
import com.danieldigiovanni.token.TokenType;

import java.util.List;

public class Parser {

    private final List<Token> tokens;

    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        try {
            return this.expression();
        } catch (LoxParseException parseException) {
            return null;
        }
    }

    /**
     * Expression rule.
     * <p>
     * The expression rule is:
     *
     * <pre>{@code
     * expression -> equality
     * }</pre>
     *
     * which means we just check for the equality rule.
     *
     * @return The expression corresponding to the given list of tokens.
     */
    private Expr expression() {
        return this.equality();
    }

    /**
     * Equality rule.
     * <p>
     * The equality rule is:
     *
     * <pre>{@code
     * equality -> comparison ( ( "!=" | "==" ) comparison )*
     * }</pre>
     *
     * which means we check for the comparison rule, followed by zero or more
     * equalities or inequalities with another comparison.
     *
     * @return The expression representing the equality rule.
     */
    private Expr equality() {
        Expr expr = this.comparison();
        Expr left = expr;

        while (this.matchTokenType(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            Token operator = this.consume();
            Expr right = this.comparison();
            expr = new Binary(left, operator, right);
        }

        return expr;
    }

    /**
     * Comparison rule.
     * <p>
     * The comparison rule is:
     *
     * <pre>{@code
     * comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )*
     * }</pre>
     *
     * which means we check for the term rule, followed by zero or more
     * comparison operators with another term.
     *
     * @return The expression representing the comparison rule.
     */
    private Expr comparison() {
        Expr expr = this.term();
        Expr left = expr;

        while (this.matchTokenType(
            TokenType.GREATER,
            TokenType.GREATER_EQUAL,
            TokenType.LESS,
            TokenType.LESS_EQUAL
        )) {
            Token operator = this.consume();
            Expr right = this.term();
            expr = new Binary(left, operator, right);
        }

        return expr;
    }

    /**
     * Term rule.
     * <p>
     * The term rule is:
     *
     * <pre>{@code
     * term -> factor ( ( "-" | "+" ) factor )*
     * }</pre>
     *
     * which means we check for the factor rule, followed by zero or more
     * addition or subtraction operators with another factor.
     *
     * @return The expression representing the term rule.
     */
    private Expr term() {
        Expr expr = this.factor();
        Expr left = expr;

        while (this.matchTokenType(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = this.consume();
            Expr right = this.factor();
            expr = new Binary(left, operator, right);
        }

        return expr;
    }

    /**
     * Factor rule.
     * <p>
     * The factor rule is:
     *
     * <pre>{@code
     * factor -> unary ( ( "*" | "/" ) unary )*
     * }</pre>
     *
     * which means we check for the unary rule, followed by zero or more
     * multiplication or division operators with another unary.
     *
     * @return The expression representing the factor rule.
     */
    private Expr factor() {
        Expr expr = this.unary();
        Expr left = expr;

        while (this.matchTokenType(TokenType.STAR, TokenType.SLASH)) {
            Token operator = this.consume();
            Expr right = this.unary();
            expr = new Binary(left, operator, right);
        }

        return expr;
    }

    /**
     * Unary rule.
     * <p>
     * The unary rule is:
     *
     * <pre>{@code
     * unary -> ( ( "!" | "-" ) unary ) | primary
     * }</pre>
     *
     * which means we check for either one of:
     * <ul>
     *     <li>a bang or minus operator followed by a unary expression, or</li>
     *     <li>a primary expression.</li>
     * </ul>
     *
     * @return The expression representing the unary rule.
     */
    private Expr unary() {
        if (this.matchTokenType(TokenType.BANG, TokenType.MINUS)) {
            Token operator = this.consume();
            Expr expr = this.unary();
            return new Unary(operator, expr);
        }

        return this.primary();
    }

    private Expr primary() {
        if (this.matchTokenType(TokenType.TRUE)) {
            this.consume();
            return new Literal(true);
        }
        if (this.matchTokenType(TokenType.FALSE)) {
            this.consume();
            return new Literal(false);
        }
        if (this.matchTokenType(TokenType.NIL)) {
            this.consume();
            return new Literal(null);
        }
        if (this.matchTokenType(TokenType.NUMBER, TokenType.STRING)) {
            Literal literal = new Literal(this.peek().getLiteral());
            this.consume();
            return literal;
        }
        if (this.matchTokenType(TokenType.LEFT_PAREN)) {
            this.consume();
            Expr expr = this.expression();
            if (!this.peek().getType().equals(TokenType.RIGHT_PAREN)) {
                throw new LoxParseException();
            }
            return new Grouping(expr);
        }
        throw new LoxParseException();
    }

    private boolean matchTokenType(TokenType... tokenTypes) {
        for (TokenType tokenType : tokenTypes) {
            if (this.peek().getType().equals(tokenType)) {
                return true;
            }
        }
        return false;
    }

    private Token consume() {
        Token token = this.peek();
        this.current++;
        return token;
    }

    private Token peek() {
        return this.tokens.get(this.current);
    }

    private void synchronize() {
        Token currentToken = this.peek();
        while (!currentToken.getType().equals(TokenType.EOF)) {
            if (currentToken.getType().equals(TokenType.SEMICOLON)) {
                this.consume();
                return;
            }

            switch (currentToken.getType()) {
                case CLASS:
                case FUN:
                case VAR:
                case IF:
                case FOR:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            this.consume();
            currentToken = this.peek();
        }
    }

}
