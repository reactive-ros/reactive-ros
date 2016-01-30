package org.reactive_ros.internal.expressions.backpressure;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.util.functions.Action0;

/**
 * @author Orestis Melkonian
 */
public class BackpressureBufferExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    private long capacity;
    private Action0 onOverflow;

    public BackpressureBufferExpr(long capacity, Action0 onOverflow) {
        this.capacity = capacity;
        this.onOverflow = onOverflow;
    }

    public long getCapacity() {
        return capacity;
    }

    public Action0 getOnOverflow() {
        return onOverflow;
    }

    @Override
    public Transformer<T> clone() {
        return new BackpressureBufferExpr<>(capacity, onOverflow);
    }
}
