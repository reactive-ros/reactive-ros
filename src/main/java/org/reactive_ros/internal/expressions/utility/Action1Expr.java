package org.reactive_ros.internal.expressions.utility;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.util.functions.Action1;

/**
 * @author Orestis Melkonian
 */
public class Action1Expr<T> extends SingleInputExpr<T> implements Transformer<T> {
    private Action1<Throwable> onErrorAction;
    private Action1<? super T> onNextAction;
    private Action1<Long> onRequestAction;

    public Action1Expr(Action1<Throwable> onErrorAction, Action1<? super T> onNextAction, Action1<Long> onRequestAction) {
                this.onErrorAction= onErrorAction;
        this.onNextAction = onNextAction;
        this.onRequestAction = onRequestAction;
    }

    public Action1<Throwable> getOnErrorAction() {
        return onErrorAction;
    }

    public Action1<? super T> getOnNextAction() {
        return onNextAction;
    }

    public Action1<Long> getOnRequestAction() {
        return onRequestAction;
    }

    @Override
    public Transformer<T> clone() {
        return new Action1Expr<>((onErrorAction != null) ? onErrorAction : null,
                (onNextAction != null) ? onNextAction : null, (onRequestAction != null) ? onRequestAction : null);
    }
}
