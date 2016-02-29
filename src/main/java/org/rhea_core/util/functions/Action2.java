package org.rhea_core.util.functions;

/**
 * Action with 2 arguments.
 * @author Orestis Melkonian
 */
public interface Action2<T1, T2> extends Action {
    void call(T1 t1, T2 t2);
}
