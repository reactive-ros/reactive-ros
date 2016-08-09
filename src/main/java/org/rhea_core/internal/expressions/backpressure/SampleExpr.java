package org.rhea_core.internal.expressions.backpressure;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

import java.util.concurrent.TimeUnit;

/**
 * @author Orestis Melkonian
 */
public class SampleExpr<T> extends SingleInputExpr implements Transformer<T> {
    private long time;
    private TimeUnit timeUnit;

    public SampleExpr(long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public long getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public Transformer<T> clone() {
        return new SampleExpr<>(time, timeUnit);
    }
}
