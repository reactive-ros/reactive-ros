package org.rhea_core.internal.expressions.creation;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class RepeatExpr<T> extends SingleInputExpr implements Transformer<T> {
    private int count;

    public RepeatExpr(int count) {
                this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public Transformer<T> clone() {
        return new RepeatExpr<>(count);
    }
}
