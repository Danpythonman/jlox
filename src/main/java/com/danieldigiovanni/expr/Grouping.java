package com.danieldigiovanni.expr;

import com.danieldigiovanni.expr.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Grouping implements Expr {

    private final Expr expr;

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

}
