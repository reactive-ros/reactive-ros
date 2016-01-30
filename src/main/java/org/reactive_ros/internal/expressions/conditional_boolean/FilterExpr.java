package org.reactive_ros.internal.expressions.conditional_boolean;

import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.util.functions.Func1;

/**
 * @author Orestis Melkonian
 */
public class FilterExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    private Func1<? super T, Boolean> predicate;

    public FilterExpr(Func1<? super T, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Func1<? super T, Boolean> getPredicate() {
        return predicate;
    }

    @Override
    public Transformer<T> clone() {
        return new FilterExpr<>(predicate);
    }
}
