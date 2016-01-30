package org.reactive_ros.internal.expressions.transformational;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.util.functions.Func2;

/**
 * @author Orestis Melkonian
 */
public class ScanExpr<T, R> extends SingleInputExpr<T> implements Transformer<R> {
    private R seed;
    private Func2<R, ? super T, R> accumulator;

    public ScanExpr(R seed, Func2<R, ? super T, R> accumulator) {
        this.seed = seed;
        this.accumulator = accumulator;
    }

    public R getSeed() {
        return seed;
    }

    public Func2<R, ? super T, R> getAccumulator() {
        return accumulator;
    }

    @Override
    public Transformer<R> clone() {
        return new ScanExpr<>(seed, accumulator);
    }
}
