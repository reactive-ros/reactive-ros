package org.rhea_core.internal.expressions.conditional_boolean;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.util.functions.Func1;

/**
 * @author Orestis Melkonian
 */
public class SkipWhileExpr<T> extends SingleInputExpr implements Transformer<T> {
    private Func1<? super T, Boolean> predicate;

    public SkipWhileExpr(Func1<? super T, Boolean> predicate) {
                this.predicate = predicate;
    }

    public Func1<? super T, Boolean> getPredicate() {
        return predicate;
    }

    @Override
    public Transformer<T> clone() {
        return new SkipWhileExpr<>(predicate);
    }
}
