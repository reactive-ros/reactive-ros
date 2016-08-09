package org.rhea_core.internal.expressions.error_handling;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class RetryExpr<T> extends SingleInputExpr implements Transformer<T> {
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
