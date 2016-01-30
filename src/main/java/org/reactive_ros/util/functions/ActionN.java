package org.reactive_ros.util.functions;

/**
 * Action with variable number of arguments.
 * @author Orestis Melkonian
 */
public interface ActionN extends Action {
    void call(Object... args);
}
