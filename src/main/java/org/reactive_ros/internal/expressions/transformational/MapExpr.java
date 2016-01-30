package org.reactive_ros.internal.expressions.transformational;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.util.functions.Func1;

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
