package com.danieldigiovanni.expr.visitor;

import com.danieldigiovanni.expr.Binary;
import com.danieldigiovanni.expr.Expr;
import com.danieldigiovanni.expr.Grouping;
import com.danieldigiovanni.expr.Literal;
import com.danieldigiovanni.expr.Unary;

public class AstPrinterVisitor implements Visitor<String> {

    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visit(Binary expr) {
        return String.format(
            "(%s %s %s)",
            expr.getOperator().getLexeme(),
            expr.getLeft().accept(this),
            expr.getRight().accept(this)
        );
    }

    @Override
    public String visit(Grouping expr) {
        return String.format("(%s)", expr.getExpr().accept(this));
    }

    @Override
    public String visit(Unary expr) {
        return String.format(
            "(%s %s)",
            expr.getOperator().getLexeme(),
            expr.getRight().accept(this)
        );
    }

    @Override
    public String visit(Literal expr) {
        return expr != null ? expr.getValue().toString() : "nil";
    }

}
