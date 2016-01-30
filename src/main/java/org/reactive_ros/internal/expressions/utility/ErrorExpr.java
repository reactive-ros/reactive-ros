package org.reactive_ros.internal.expressions.utility;

import org.reactive_ros.internal.expressions.NoInputExpr;
import org.reactive_ros.internal.expressions.Transformer;

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
