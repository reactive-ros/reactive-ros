package org.rhea_core.internal.expressions.transformational;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.util.functions.Func2;

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
