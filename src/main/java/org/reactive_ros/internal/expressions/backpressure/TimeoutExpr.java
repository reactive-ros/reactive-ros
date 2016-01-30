package org.reactive_ros.internal.expressions.backpressure;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;

import java.util.concurrent.TimeUnit;

/**
 * @author Orestis Melkonian
 */
public class TimeoutExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    private long time;
    private TimeUnit timeUnit;

    public TimeoutExpr(long time, TimeUnit timeUnit) {
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
        return new TimeoutExpr<>(time, timeUnit);
    }
}
