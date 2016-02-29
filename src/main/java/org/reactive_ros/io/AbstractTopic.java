package org.reactive_ros.io;

import org.reactive_ros.Stream;
import org.reactive_ros.evaluation.Serializer;
import org.reactive_ros.internal.expressions.creation.FromSource;
import org.reactive_ros.internal.output.MultipleOutput;
import org.reactive_ros.internal.output.Output;
import org.reactive_ros.internal.output.SinkOutput;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Orestis Melkonian
 */
public abstract class AbstractTopic<T, B, C> implements Source<T>, Sink<T> {

    protected String name;
    protected Serializer<B> serializer;

    public abstract void setClient(C client);

    protected AbstractTopic(String name, Serializer<B> serializer) {
        this.name = name;
        this.serializer = serializer;
    }

    public String getName() {
        return name;
    }

    public String name() {
        return name + "[" + Thread.currentThread().getId() + "]";
    }

    /**
     * Extracts all {@link AbstractTopic}s involved in the given dataflow graph.
     * @param stream the {@link Stream} defined by the user
     * @param output the {@link Output} to re-direct the evaluated result
     * @return all {@link AbstractTopic}s as a {@link List}
     */
    public static List<AbstractTopic> extract(Stream stream, Output output) {
        List<AbstractTopic> topics = stream.getGraph()
                .vertexSet()
                .stream()
                .filter(n -> n instanceof FromSource)
                .map(n -> ((FromSource) n).getSource())
                .filter(s -> s instanceof AbstractTopic)
                .map(s -> ((AbstractTopic) s))
                .collect(Collectors.toList());

        if (output instanceof MultipleOutput) {
            for (Output out : ((MultipleOutput) output).getOutputs())
                if (out instanceof SinkOutput && ((SinkOutput) out).getSink() instanceof AbstractTopic)
                    topics.add((AbstractTopic) ((SinkOutput) out).getSink());
        }
        else if (output instanceof SinkOutput && ((SinkOutput) output).getSink() instanceof AbstractTopic)
            topics.add((AbstractTopic) ((SinkOutput) output).getSink());

        return topics;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof AbstractTopic
                && obj.getClass() == this.getClass()
                && name.equals(((AbstractTopic) obj).getName());
    }
}
