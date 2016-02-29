package org.rhea_core.internal.expressions.combining;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class MergeExpr<T> extends SingleInputExpr<Transformer<T>> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new MergeExpr<>();
    }
}
