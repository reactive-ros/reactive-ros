package org.rhea_core.internal.expressions.utility;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.util.functions.Action0;

/**
 * @author Orestis Melkonian
 */
public class Action0Expr<T> extends SingleInputExpr implements Transformer<T> {
    private Action0 action;
    private String when;

    public Action0Expr(Action0 action, String when) {
        this.action = action;
        this.when = when;
    }

    public Action0 getAction() {
        return action;
    }

    public String getWhen() {
        return when;
    }

    @Override
    public Transformer<T> clone() {
        return new Action0Expr<>(action, when);
    }
}
