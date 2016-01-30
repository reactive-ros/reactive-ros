package org.reactive_ros.internal.expressions.conditional_boolean;

import org.reactive_ros.internal.expressions.MultipleInputExpr;
import org.reactive_ros.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class AmbExpr<T> extends MultipleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new AmbExpr<>();
    }
}
