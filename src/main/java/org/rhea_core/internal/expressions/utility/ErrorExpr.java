package org.rhea_core.internal.expressions.utility;

import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class ErrorExpr<T> extends NoInputExpr<T> implements Transformer<T> {
    private Throwable t;

    public ErrorExpr(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return t;
    }

    @Override
    public Transformer<T> clone() {
        return new ErrorExpr<>(t);
    }
}
