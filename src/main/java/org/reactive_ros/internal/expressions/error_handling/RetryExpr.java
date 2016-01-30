package org.reactive_ros.internal.expressions.error_handling;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class RetryExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    private int count;

    public RetryExpr(int count) {
                this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public Transformer<T> clone() {
        return new RetryExpr<>(count);
    }
}
