package org.rhea_core.internal.expressions.creation;

import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.Transformer;

import java.util.concurrent.TimeUnit;

/**
 * @author Orestis Melkonian
 */
public class IntervalExpr extends NoInputExpr implements Transformer<Long> {
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
