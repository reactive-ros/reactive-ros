package org.reactive_ros.internal.expressions.creation;

import org.reactive_ros.internal.expressions.NoInputExpr;
import org.reactive_ros.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class EmptyExpr<T> extends NoInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new EmptyExpr<>();
    }
}
