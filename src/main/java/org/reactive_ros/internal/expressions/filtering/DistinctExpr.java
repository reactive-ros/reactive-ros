package org.reactive_ros.internal.expressions.filtering;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class DistinctExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new DistinctExpr<>();
    }
}
