package org.rhea_core.internal.expressions.filtering;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class TakeExpr<T> extends SingleInputExpr implements Transformer<T> {
    private int count;

    public TakeExpr(int count) {
                this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public Transformer<T> clone() {
        return new TakeExpr<>(count);
    }
}
