package org.reactive_ros.internal.expressions;

/**
 * A {@link Transformer} that has a single other {@link Transformer} as input.
 * @author Orestis Melkonian
 */
public class SingleInputExpr<T> {
    @Override
    public String toString() {
        return getClass().getSimpleName().replaceFirst("Expr", "");
    }
}
