package org.rhea_core.internal.expressions.creation;

import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class EmptyExpr<T> extends NoInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new EmptyExpr<>();
    }
}
