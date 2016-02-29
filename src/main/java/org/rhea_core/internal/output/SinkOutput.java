package org.rhea_core.internal.output;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.io.Sink;

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
