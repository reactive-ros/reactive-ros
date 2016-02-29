package org.rhea_core.internal.expressions.utility;

import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class SerializeExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new SerializeExpr<>();
    }
}
