package org.rhea_core.internal.expressions.transformational;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.util.functions.Func2;

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
