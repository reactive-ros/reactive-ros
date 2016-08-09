package org.rhea_core.internal.expressions.backpressure;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.util.functions.Action1;

/**
 * @author Orestis Melkonian
 */
public class BackpressureDropExpr<T> extends SingleInputExpr implements Transformer<T> {
    private Action1<? super T> onDrop;

    public BackpressureDropExpr(Action1<? super T> onDrop) {
        this.onDrop = onDrop;
    }

    public Action1<? super T> getOnDrop() {
        return onDrop;
    }

    @Override
    public Transformer<T> clone() {
        return new BackpressureDropExpr<>(onDrop);
    }
}
