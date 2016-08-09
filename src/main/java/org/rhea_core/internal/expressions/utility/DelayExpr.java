package org.rhea_core.internal.expressions.utility;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

import java.util.concurrent.TimeUnit;

/**
 * @author Orestis Melkonian
 */
public class DelayExpr<T> extends SingleInputExpr implements Transformer<T> {
    private long delay;
    private TimeUnit unit;

    public DelayExpr(long delay, TimeUnit unit) {
        this.delay = delay;
        this.unit = unit;
    }

    public long getDelay() {
        return delay;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    @Override
    public Transformer<T> clone() {
        return new DelayExpr<>(delay, unit);
    }
}
