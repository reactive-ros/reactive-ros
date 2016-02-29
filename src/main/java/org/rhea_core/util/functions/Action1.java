package org.rhea_core.util.functions;

/**
 * Action with 1 argument.
 * @author Orestis Melkonian
 */
public interface Action1<T> extends Action {
    void call(T t);
}
