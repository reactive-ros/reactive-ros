package org.rhea_core.internal.expressions.backpressure;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.util.functions.Action0;

/**
 * @author Orestis Melkonian
 */
public class BackpressureBufferExpr<T> extends SingleInputExpr implements Transformer<T> {
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
