package com.danieldigiovanni.expr;

import com.danieldigiovanni.expr.visitor.Visitor;

public interface Expr {

    <T> T accept(Visitor<T> visitor);

}
