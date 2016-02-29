package org.rhea_core.internal.expressions.backpressure;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class BackpressureLatestExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new BackpressureLatestExpr<>();
    }
}
