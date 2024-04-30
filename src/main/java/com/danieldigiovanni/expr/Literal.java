package com.danieldigiovanni.expr;

import com.danieldigiovanni.expr.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Literal implements Expr {

    private final Object value;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

}
