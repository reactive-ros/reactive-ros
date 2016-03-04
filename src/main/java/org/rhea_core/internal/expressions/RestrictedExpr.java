package org.rhea_core.internal.expressions;

import org.rhea_core.evaluation.EvaluationStrategy;

/**
 * A {@link Transformer} that can only be evaluated by a certain {@link EvaluationStrategy}.
 * @author Orestis Melkonian
 */
public class RestrictedExpr<T> {
    String id;

    public RestrictedExpr(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName().replaceFirst("Expr", "");
    }
}
