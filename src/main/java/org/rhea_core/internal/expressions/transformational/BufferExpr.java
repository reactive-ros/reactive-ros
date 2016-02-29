package org.rhea_core.internal.expressions.transformational;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Orestis Melkonian
 */
public class BufferExpr<T> extends SingleInputExpr<T> implements Transformer<List<T>> {
    private int count;
    private long timespan;
    private TimeUnit unit;

    public BufferExpr(int count, long timespan, TimeUnit unit) {
        this.count = count;
        this.timespan = timespan;
        this.unit = unit;
    }

    public int getCount() {
        return count;
    }

    public long getTimespan() {
        return timespan;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    @Override
    public Transformer<List<T>> clone() {
        return new BufferExpr<>(count, timespan, unit);
    }
}
