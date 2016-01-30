package org.reactive_ros.util.functions;

import java.util.concurrent.Callable;

/**
 * Function with 0 arguments.
 * @author Orestis Melkonian
 */
public interface Func0<R> extends Function, Callable<R> {
    @Override
    R call();
}
