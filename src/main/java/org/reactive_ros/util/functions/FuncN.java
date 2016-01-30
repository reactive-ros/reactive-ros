package org.reactive_ros.util.functions;

/**
 * Function with variable number of arguments.
 * <p>
 *     No type inference provided.
 * </p>
 * @author Orestis Melkonian
 */
public interface FuncN<R> extends Function {
    R call(Object... args);
}
