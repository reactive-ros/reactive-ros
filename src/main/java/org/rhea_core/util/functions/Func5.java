package org.rhea_core.util.functions;

/**
 * Function with 5 arguments.
 * @author Orestis Melkonian
 */
public interface Func5<T1, T2, T3, T4, T5, R> extends Function {
    R call(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
}
