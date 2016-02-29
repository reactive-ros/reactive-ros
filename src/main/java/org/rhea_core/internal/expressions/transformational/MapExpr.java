package org.rhea_core.internal.expressions.transformational;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.util.functions.Func1;

/**
 * @author Orestis Melkonian
 */
public class MapExpr<T, R> extends SingleInputExpr<T> implements Transformer<R> {
    private Func1<? super T, ? extends R> mapper;

    public MapExpr(Func1<? super T, ? extends R> mapper) {
        this.mapper = mapper;
    }

    public Func1<? super T, ? extends R> getMapper() { return mapper; }

    @Override
    public Transformer<R> clone() {
        return new MapExpr<>(mapper);
    }
}
