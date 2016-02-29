package org.rhea_core.internal.expressions.feedback;

import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class EntryPointExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer clone() {
        return new EntryPointExpr<>();
    }
}
