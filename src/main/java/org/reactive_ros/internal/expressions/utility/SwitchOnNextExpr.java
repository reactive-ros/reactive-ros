package org.reactive_ros.internal.expressions.utility;

import org.reactive_ros.Stream;
import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class SwitchOnNextExpr<T> extends SingleInputExpr<Stream<T>> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new SwitchOnNextExpr<>();
    }
}
