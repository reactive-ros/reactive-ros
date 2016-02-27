package org.reactive_ros.io;

import org.reactivestreams.Subscriber;

import java.io.Serializable;

/**
 * All outputs should implement this interface.
 * @author Orestis Melkonian
 */
public interface Sink<T> extends Subscriber<T>, Serializable {
}
