package org.reactive_ros.internal.expressions.creation;

import org.reactive_ros.Stream;
import org.reactive_ros.internal.expressions.NoInputExpr;
import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.util.functions.Func0;

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
