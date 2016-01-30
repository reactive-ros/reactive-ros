package org.reactive_ros.internal.expressions.feedback;

import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class ExitPointExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer clone() {
        return new ExitPointExpr<>();
    }
}
