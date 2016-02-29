package org.rhea_core.util.functions;

/**
 * Action with 4 arguments.
 * @author Orestis Melkonian
 */
public interface Action4<T1, T2, T3, T4> extends Action {
    void call(T1 t1, T2 t2, T3 t3, T4 t4);
}
