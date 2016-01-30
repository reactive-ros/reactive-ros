package org.reactive_ros.internal.expressions.creation;

import org.reactive_ros.internal.expressions.NoInputExpr;
import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.io.Listener;
import org.reactive_ros.internal.io.Source;

/**
 * @author Orestis Melkonian
 */
public class FromListener<T> extends NoInputExpr<T> implements Transformer<T> {
    private Listener<? extends T> source;

    public FromListener(Listener<? extends T> source) {
        this.source = source;
    }

    public Listener<? extends T> getSource() {
        return source;
    }

    @Override
    public Transformer<T> clone() {
        return new FromListener<>(source);
    }

    @Override
    public String toString() {
        return "FromSource: " + source.getClass().getTypeParameters()[0].getName();
    }
}
