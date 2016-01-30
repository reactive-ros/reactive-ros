package org.reactive_ros.internal.expressions.backpressure;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class BackpressureLatestExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new BackpressureLatestExpr<>();
    }
}
