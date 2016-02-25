package org.reactive_ros.internal.output;

import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.io.Sink;

/**
 * Redirects a {@link Transformer}'s resulting stream to a ROS topic.
 * @author Orestis Melkonian
 */
public class SinkOutput<T> implements Output {
    private Sink<T> sink;

    public SinkOutput(Sink<T> sink) {
        this.sink = sink;
    }

    public Sink<T> getSink() {
        return sink;
    }

    @Override
    public String toString() {
        return sink.toString();
    }
}
