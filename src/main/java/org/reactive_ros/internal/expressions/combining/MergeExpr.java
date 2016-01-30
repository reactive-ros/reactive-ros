package org.reactive_ros.internal.expressions.combining;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class MergeExpr<T> extends SingleInputExpr<Transformer<T>> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new MergeExpr<>();
    }
}
