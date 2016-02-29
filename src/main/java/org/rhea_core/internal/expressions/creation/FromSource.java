package org.rhea_core.internal.expressions.creation;

import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.io.Source;

/**
 * @author Orestis Melkonian
 */
public class FromSource<T> extends NoInputExpr<T> implements Transformer<T> {
    private Source<T> source;

    public FromSource(Source<T> source) {
        this.source = source;
    }

    public Source<T> getSource() {
        return source;
    }

    @Override
    public Transformer<T> clone() {
        return new FromSource<>(source);
    }

    @Override
    public String toString() {
        return "FromSource: " + source;
    }
}
