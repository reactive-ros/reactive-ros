package org.reactive_ros.internal.expressions.utility;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.util.functions.Action0;

/**
 * @author Orestis Melkonian
 */
public class Action0Expr<T> extends SingleInputExpr<T> implements Transformer<T> {
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
