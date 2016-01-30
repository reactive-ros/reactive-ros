package org.reactive_ros.internal.expressions.combining;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class ConcatExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new ConcatExpr<>();
    }
}

