package com.danieldigiovanni.interpreter;

import com.danieldigiovanni.expr.Binary;
import com.danieldigiovanni.expr.Expr;
import com.danieldigiovanni.expr.Grouping;
import com.danieldigiovanni.expr.Literal;
import com.danieldigiovanni.expr.Unary;
import com.danieldigiovanni.expr.visitor.Visitor;
import com.danieldigiovanni.interpreter.exception.LoxRuntimeException;
import com.danieldigiovanni.interpreter.exception.LoxBinaryOperandsTypeRuntimeException;
import com.danieldigiovanni.interpreter.exception.LoxPlusOperandsTypeRuntimeRuntimeException;
import com.danieldigiovanni.interpreter.exception.LoxUnaryOperandTypeRuntimeException;
import com.danieldigiovanni.interpreter.exception.LoxUnknownBinaryOperatorRuntimeException;
import com.danieldigiovanni.interpreter.exception.LoxUnknownUnaryOperatorRuntimeException;
import com.danieldigiovanni.token.Token;

public class Interpreter implements Visitor<Object> {

    /**
     * Evaluates a Lox expression.
     * <p>
     * The result of the expression is printed as a string.
     *
     * @param expr The Lox expression to be evaluated.
     */
    public void interpret(Expr expr) {
        try {
            Object value = this.evaluate(expr);
            System.out.println(this.stringify(value));
        } catch (LoxRuntimeException exception) {
            throw exception;
        }
    }

    @Override
    public Object visit(Binary expr) {
        Object left = this.evaluate(expr.getLeft());
        Object right = this.evaluate(expr.getRight());

        Token operator = expr.getOperator();

        this.checkBinaryOperatorTypes(left, operator, right);

        switch (operator.getType()) {
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return ((Double) left) + ((Double) right);
                }
                if (left instanceof String && right instanceof String) {
                    return ((String) left) + ((String) right);
                }
            case MINUS:
                return ((Double) left) - ((Double) right);
            case STAR:
                return ((Double) left) * ((Double) right);
            case SLASH:
                return ((Double) left) / ((Double) right);
            case GREATER:
                return ((Double) left) > ((Double) right);
            case GREATER_EQUAL:
                return ((Double) left) >= ((Double) right);
            case LESS:
                return ((Double) left) < ((Double) right);
            case LESS_EQUAL:
                return ((Double) left) <= ((Double) right);
            case EQUAL_EQUAL:
                return equal(left, right);
            case BANG_EQUAL:
                return !equal(left, right);
            default:
                throw new LoxUnknownBinaryOperatorRuntimeException(operator);
        }
    }

    @Override
    public Object visit(Grouping expr) {
        return this.evaluate(expr.getExpr());
    }

    @Override
    public Object visit(Unary expr) {
        Object right = this.evaluate(expr.getRight());
        Token operator = expr.getOperator();

        switch (operator.getType()) {
            case BANG:
                return truthy(right);
            case MINUS:
                if (!(right instanceof Double)) {
                    throw new LoxUnaryOperandTypeRuntimeException(operator);
                }
                return -((Double) right);
            default:
                throw new LoxUnknownUnaryOperatorRuntimeException(operator);
        }
    }

    @Override
    public Object visit(Literal expr) {
        return expr.getValue();
    }

    /**
     * Evaluates an expression.
     * <p>
     * The expression is evaluated by calling the {@link Expr#accept(Visitor)}
     * method on the expression.
     *
     * @param expr The expression to be evaluated.
     *
     * @return The result of the evaluation.
     */
    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    /**
     * Converts a Lox object to a boolean value.
     * <p>
     * A Lox object is considered falsy if it is {@code null} or if it has the
     * boolean value {@code false}. <b>All other values are considered
     * truthy</b> (even numeric {@code 0}, empty strings, etc.).
     *
     * @param object The Lox object to be converted to a boolean value.
     *
     * @return The boolean representation of the Lox object.
     */
    private Boolean truthy(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        return true;
    }

    /**
     * Checks if two Lox objects are equal.
     * <p>
     * Two Lox objects are considered equal if they are both {@code null} or if
     * {@code a.equals(b)} returns {@code true}.
     *
     * @param a The first Lox object.
     * @param b The second Lox object.
     *
     * @return {@code true} if the two Lox objects are equal, {@code false}
     *         otherwise.
     */
    private Boolean equal(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    /**
     * Converts a Lox object to a string.
     *
     * @param object The Lox object to be converted to a string.
     *
     * @return The string representation of the Lox object.
     */
    private String stringify(Object object) {
        if (object == null) {
            return "nil";
        }

        String text = object.toString();

        if (object instanceof Double) {
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
        }

        return text;
    }

    /**
     * Checks the operand types of a binary operation (represented by the
     * operator) to ensure that the types are correct. A
     * {@link LoxBinaryOperandsTypeRuntimeException} is thrown if the types are
     * incorrect.
     *
     * @param left The left operand.
     * @param operator The operator.
     * @param right The right operand.
     *
     * @throws LoxBinaryOperandsTypeRuntimeException If the types of the
     *                                               operands are incorrect
     *                                               with respect to the
     *                                               operator.
     */
    private void checkBinaryOperatorTypes(Object left, Token operator, Object right) throws LoxBinaryOperandsTypeRuntimeException {
        switch (operator.getType()) {
            case PLUS:
                // Plus operation can operate on two numbers or two strings
                if (
                    !(left instanceof String && right instanceof String)
                        && !(left instanceof Double && right instanceof Double)
                ) {
                    throw new LoxPlusOperandsTypeRuntimeRuntimeException(
                        operator
                    );
                }
                return;
            case MINUS:
            case STAR:
            case SLASH:
            case GREATER:
            case GREATER_EQUAL:
            case LESS:
            case LESS_EQUAL:
                // These operations can operate on two numbers
                if (!(left instanceof Double && right instanceof Double)) {
                    throw new LoxBinaryOperandsTypeRuntimeException(operator);
                }
                return;
            case EQUAL_EQUAL:
            case BANG_EQUAL:
            default:
                // No checks needed because these operations can operate on any
                // types
        }
    }

}
