package com.danieldigiovanni.expr.visitor;

import com.danieldigiovanni.expr.Binary;
import com.danieldigiovanni.expr.Grouping;
import com.danieldigiovanni.expr.Literal;
import com.danieldigiovanni.expr.Unary;

public interface Visitor<T> {

    T visit(Binary expr);

    T visit(Grouping expr);

    T visit(Unary expr);

    T visit(Literal expr);

}
