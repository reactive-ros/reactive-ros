package org.reactive_ros.io;

import org.reactive_ros.Stream;
import org.reactive_ros.util.functions.Action1;

import java.io.Serializable;

/**
 * @author Orestis Melkonian
 */
public interface Listener<T> extends Serializable {

    /**
     * Provides the means to register a listener pushing values to a {@link Stream}.
     * @param action this will be passed from the streams API and has to be called for every new message
     */
    void register(Action1<T> action);
}
