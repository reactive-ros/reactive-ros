package org.reactive_ros.internal.expressions.creation;

import org.reactive_ros.internal.expressions.NoInputExpr;
import org.reactive_ros.internal.expressions.Transformer;

import java.util.concurrent.TimeUnit;

/**
 * @author Orestis Melkonian
 */
public class IntervalExpr extends NoInputExpr<Long> implements Transformer<Long> {
    private long interval;
    private TimeUnit unit;

    public IntervalExpr(long interval, TimeUnit unit) {
        this.interval = interval;
        this.unit = unit;
    }

    public long getInterval() {
        return interval;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    @Override
    public Transformer<Long> clone() {
        return new IntervalExpr(interval, unit);
    }
}
