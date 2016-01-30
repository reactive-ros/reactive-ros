package org.reactive_ros.util.functions;

/**
 * Action with 5 arguments.
 * @author Orestis Melkonian
 */
public interface Action5<T1, T2, T3, T4, T5> extends Action {
    void call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
}
