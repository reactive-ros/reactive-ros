package org.rhea_core.internal.expressions.combining;

import org.rhea_core.internal.expressions.MultipleInputExpr;
import org.rhea_core.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class ConcatMultiExpr<T> extends MultipleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new ConcatMultiExpr<>();
    }
}

