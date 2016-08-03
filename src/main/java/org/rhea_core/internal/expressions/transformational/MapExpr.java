package org.rhea_core.internal.expressions.transformational;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.internal.expressions.combining.ZipExpr;
import org.rhea_core.util.functions.Func1;

/**
 * @author Orestis Melkonian
 */
public class MapExpr<T, R> extends SingleInputExpr<T> implements Transformer<R> {
    private int id;
    private Func1<? super T, ? extends R> mapper;

    public MapExpr(int id, Func1<? super T, ? extends R> mapper) {
        this.id = id;
        this.mapper = mapper;
    }

    public Func1<? super T, ? extends R> getMapper() { return mapper; }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof MapExpr))
            return false;
        return id == ((MapExpr) obj).id;
    }

    @Override
    public Transformer<R> clone() {
        return new MapExpr<>(id, mapper);
    }

    @Override
    public String toString() {
        return super.toString() + id;
    }
}
