package org.reactive_ros.util.functions;

import java.util.concurrent.Callable;

/**
 * Action with 0 arguments.
 * @author Orestis Melkonian
 */
public interface Action0 extends Action {
    void call();
}
