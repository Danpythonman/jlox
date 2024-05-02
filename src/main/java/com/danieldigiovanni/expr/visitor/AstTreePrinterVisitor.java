package com.danieldigiovanni.expr.visitor;

import com.danieldigiovanni.expr.Binary;
import com.danieldigiovanni.expr.Expr;
import com.danieldigiovanni.expr.Grouping;
import com.danieldigiovanni.expr.Literal;
import com.danieldigiovanni.expr.Unary;

public class AstTreePrinterVisitor implements Visitor<String> {

    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visit(Binary expr) {
        String s1 = expr.getLeft().accept(this);
        String s2 = expr.getRight().accept(this);
        String operand = this.combine(s1, s2);
        int maxLineLength = this.maxLineLength(operand);

        StringBuilder s = new StringBuilder();
        if (expr.getOperator().getLexeme().length() == 1) {
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("--------");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("|  "     );
            s.append(" ");
            s.append(expr.getOperator().getLexeme());
            s.append(     "  |");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("--------");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("    |   ");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("    |   ");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
        } else {
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("--------");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("|  "     );
            s.append(expr.getOperator().getLexeme());
            s.append(     "  |");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("--------");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("    |   ");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("    |   ");
            s.append(" ".repeat(maxLineLength/2 - 4));
            s.append("\n");
        }
        s.append(operand);
        return s.toString();
    }

    private String combine(String s1, String s2) {
        int m1 = this.maxLineLength(s1);
        int m2 = this.maxLineLength(s2);

        String[] l1 = s1.split("\\n");
        String[] l2 = s2.split("\\n");

        StringBuilder s = new StringBuilder();

        s.append(" ".repeat(m1/2));
        s.append("|");
        s.append("-".repeat(m1/2));
        s.append("-".repeat(m2/2));
        s.append("|");
        s.append(" ".repeat(m2/2));
        s.append("\n");

        s.append(" ".repeat(m1/2));
        s.append("|");
        s.append(" ".repeat(m1/2));
        s.append(" ".repeat(m2/2));
        s.append("|");
        s.append(" ".repeat(m2/2));
        s.append("\n");

        int i=0, j=0;
        while (i < l1.length && j < l2.length) {
            s.append(l1[i]);
            s.append("  ");
            s.append(l2[j]);
            s.append("\n");
            i++;
            j++;
        }
        while (i < l1.length) {
            s.append(l1[i]);
            s.append("  ");
            s.append(" ".repeat(m2));
            s.append("\n");
            i++;
        }
        while (j < l2.length) {
            s.append(" ".repeat(m1));
            s.append("  ");
            s.append(l2[j]);
            s.append("\n");
            j++;
        }
        return s.toString();
    }

    @Override
    public String visit(Grouping expr) {
        StringBuilder s = new StringBuilder();
        String operand = expr.getExpr().accept(this);
        int maxLineLength = this.maxLineLength(operand);
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("----------");
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("\n");
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("|  (  )  |");
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("\n");
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("----------");
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("\n");
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("    |   ");
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("\n");
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("    |   ");
        s.append(" ".repeat(maxLineLength/2 - 5));
        s.append("\n");
        s.append(operand);
        return s.toString();
    }

    @Override
    public String visit(Unary expr) {
        StringBuilder s = new StringBuilder();
        String operand = expr.getRight().accept(this);
        int maxLineLength = this.maxLineLength(operand);
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("--------");
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("\n");
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("|  "     );
        s.append(expr.getOperator().getLexeme());
        s.append(     "  |");
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("\n");
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("--------");
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("\n");
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("    |   ");
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("\n");
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("    |   ");
        s.append(" ".repeat(maxLineLength/2 - 4));
        s.append("\n");
        s.append(operand);
        return s.toString();
    }

    @Override
    public String visit(Literal expr) {
        StringBuilder s = new StringBuilder();
        if (expr == null) {
            s.append("-------------\n");
            s.append("|    nil    |\n");
            s.append("-------------\n");
        } else {
            int len = expr.getValue().toString().length();
            s.append("-----");
            s.append("-".repeat(len));
            s.append("-----\n");
            s.append("|   ");
            s.append(" ");
            s.append(expr.getValue().toString());
            s.append(" ");
            s.append("   |\n");
            s.append("-----");
            s.append("-".repeat(len));
            s.append("-----\n");
        }
        return s.toString();
    }

    private int maxLineLength(String s) {
        int max = 0;
        int current = 0;
        char c;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\n') {
                max = current > max ? current : max;
                current = 0;
            } else {
                current++;
            }
        }
        return max;
    }

}
