package org.reactive_ros.internal.expressions.combining;

import org.reactive_ros.internal.expressions.MultipleInputExpr;
import org.reactive_ros.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class ConcatMultiExpr<T> extends MultipleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new ConcatMultiExpr<>();
    }
}

