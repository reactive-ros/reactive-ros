package org.rhea_core.internal.expressions;


/**
 * A {@link Transformer} that has multiple other {@link Transformer}s as input.
 * @author Orestis Melkonian
 */
public class MultipleInputExpr<T> {
    public int orderRef = 0;
    @Override
    public String toString() {
        return getClass().getSimpleName().replaceFirst("Expr", "");
    }
}
