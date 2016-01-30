package org.reactive_ros.internal.expressions.error_handling;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.util.functions.Func1;

/**
 * @author Orestis Melkonian
 */
public class OnErrorReturnExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    private Func1<Throwable, ? extends T> resumeFunction;

    public OnErrorReturnExpr(Func1<Throwable, ? extends T> resumeFunction) {
        this.resumeFunction = resumeFunction;
    }

    public Func1<Throwable, ? extends T> getResumeFunction() {
        return resumeFunction;
    }

    @Override
    public Transformer<T> clone() {
        return new OnErrorReturnExpr<>(resumeFunction);
    }
}
