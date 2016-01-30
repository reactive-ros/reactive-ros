package org.reactive_ros.internal.expressions.transformational;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.util.functions.Func2;

/**
 * @author Orestis Melkonian
 */
public class SimpleScanExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    private Func2<T, T, T> accumulator;

    public SimpleScanExpr(Func2<T, T, T> accumulator) {
        this.accumulator = accumulator;
    }

    public Func2<T, T, T> getAccumulator() {
        return accumulator;
    }

    @Override
    public Transformer<T> clone() {
        return new SimpleScanExpr<>(accumulator);
    }
}
