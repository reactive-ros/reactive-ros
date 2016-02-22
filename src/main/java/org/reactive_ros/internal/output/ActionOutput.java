package org.reactive_ros.internal.output;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.util.functions.Action0;
import org.reactive_ros.util.functions.Action1;

import java.io.Serializable;

/**
 * Applies an {@link org.reactive_ros.util.functions.Action} to a {@link Transformer}'s resulting stream.
 * @author Orestis Melknonian
 */
public class ActionOutput<T> implements Output {
    private Action1<? super T> action;
    private Action1<Throwable> errorAction;
    private Action0 completeAction;

    public ActionOutput(Action1<? super T> action) {
        this.action = action;
    }

    public ActionOutput(Action1<? super T> action, Action1<Throwable> errorAction) {
        this.action = action;
        this.errorAction = errorAction;
    }

    public ActionOutput(Action1<? super T> action, Action1<Throwable> errorAction, Action0 completeAction) {
        this.action = action;
        this.errorAction = errorAction;
        this.completeAction = completeAction;
    }

    public Action1<? super T> getAction() {
        return action;
    }

    public Action1<Throwable> getErrorAction() {
        return errorAction;
    }

    public Action0 getCompleteAction() {
        return completeAction;
    }

    @Override
    public String toString() {
        return "Action";
    }
}
