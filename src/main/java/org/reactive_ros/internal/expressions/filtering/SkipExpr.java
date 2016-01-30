package org.reactive_ros.internal.expressions.filtering;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class SkipExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    private int count;

    public SkipExpr(int count) {
                this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public Transformer<T> clone() {
        return new SkipExpr<>(count);
    }
}
