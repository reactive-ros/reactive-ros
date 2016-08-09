package org.rhea_core.internal.expressions.creation;

import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.io.Listener;

/**
 * @author Orestis Melkonian
 */
public class FromListener<T> extends NoInputExpr implements Transformer<T> {
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
