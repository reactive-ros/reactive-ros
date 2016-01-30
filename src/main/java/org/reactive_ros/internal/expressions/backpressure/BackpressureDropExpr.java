package org.reactive_ros.internal.expressions.backpressure;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.util.functions.Action1;

/**
 * @author Orestis Melkonian
 */
public class BackpressureDropExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
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
