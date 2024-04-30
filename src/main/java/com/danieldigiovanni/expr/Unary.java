package com.danieldigiovanni.expr;

import com.danieldigiovanni.expr.visitor.Visitor;
import com.danieldigiovanni.token.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Unary implements Expr {

    private final Token operator;
    private final Expr right;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

}
