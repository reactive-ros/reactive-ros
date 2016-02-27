package org.reactive_ros.io;

import org.reactivestreams.Publisher;

import java.io.Serializable;

/**
 * All inputs should implement this interface.
 * @author Orestis Melkonian
 */
public interface Source<T> extends Publisher<T>, Serializable {
}
