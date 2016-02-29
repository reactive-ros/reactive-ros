package org.rhea_core.internal.expressions;

/**
 * Represents a pipeline that acts as input and does not have any other streams as input.
 * @author Orestis Melkonian
 */
public class NoInputExpr<T> {
    @Override
    public String toString() {
        return getClass().getSimpleName().replaceFirst("Expr", "");
    }
}
