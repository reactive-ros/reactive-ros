package org.rhea_core.internal.expressions.error_handling;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.util.functions.Func1;

/**
 * @author Orestis Melkonian
 */
public class OnErrorReturnExpr<T> extends SingleInputExpr implements Transformer<T> {
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
