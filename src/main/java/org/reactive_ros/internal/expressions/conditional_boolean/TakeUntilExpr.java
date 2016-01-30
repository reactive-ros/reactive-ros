package org.reactive_ros.internal.expressions.conditional_boolean;

import org.reactive_ros.Stream;
import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class TakeUntilExpr <T, U> extends SingleInputExpr<T> implements Transformer<T> {
    private Stream<U> other;

    public TakeUntilExpr(Stream<U> other) {
                this.other = other;
    }

    public Stream<U> getOther() {
        return other;
    }

    @Override
    public Transformer<T> clone() {
        return new TakeUntilExpr<>(other.clone());
    }
}
