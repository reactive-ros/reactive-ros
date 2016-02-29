package org.rhea_core.util.functions;

/**
 * Function with 2 arguments.
 * @author Orestis Melkonian
 */
public interface Func2<T1, T2, R> extends Function {
    R call(T1 t1, T2 t2);
}
