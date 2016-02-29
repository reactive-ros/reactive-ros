package org.rhea_core.internal.expressions.filtering;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class DistinctExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new DistinctExpr<>();
    }
}
