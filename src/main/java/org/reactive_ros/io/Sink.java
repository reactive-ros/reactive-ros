package org.reactive_ros.io;

import org.reactivestreams.Subscriber;

/**
 * All outputs should implement this interface.
 * @author Orestis Melkonian
 */
public interface Sink<T> extends Subscriber<T> {
}
