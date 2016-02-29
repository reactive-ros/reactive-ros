package org.rhea_core.internal.expressions.conditional_boolean;

import org.rhea_core.internal.expressions.MultipleInputExpr;
import org.rhea_core.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class AmbExpr<T> extends MultipleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new AmbExpr<>();
    }
}
