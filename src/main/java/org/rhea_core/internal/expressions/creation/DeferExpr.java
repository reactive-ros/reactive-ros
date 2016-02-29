package org.rhea_core.internal.expressions.creation;

import org.rhea_core.Stream;
import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.util.functions.Func0;

/**
 * @author Orestis Melkonian
 */
public class DeferExpr<T> extends NoInputExpr<T> implements Transformer<T> {
    private Func0<Stream<T>> streamFactory;

    public DeferExpr(Func0<Stream<T>> streamFactory) {
        this.streamFactory = streamFactory;
    }

    public Func0<Stream<T>> getStreamFactory() {
        return streamFactory;
    }

    @Override
    public Transformer<T> clone() {
        return new DeferExpr<>(streamFactory);
    }
}
