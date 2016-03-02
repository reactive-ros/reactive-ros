package org.rhea_core.internal.expressions.filtering;

import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.util.functions.Func1;

/**
 * @author Orestis Melkonian
 */
public class FilterMapExpr<T, R> extends SingleInputExpr<T> implements Transformer<R> {
    private Func1<T, R> transform;
    private Func1<? super R, Boolean> predicate;

    public FilterMapExpr(Func1<T, R> transform, Func1<? super R, Boolean> predicate) {
        this.transform = transform;
        this.predicate = predicate;
    }

    public Func1<? super R, Boolean> getPredicate() {
        return predicate;
    }

    @Override
    public Transformer<R> clone() {
        return new FilterMapExpr<>(transform, predicate);
    }
}
