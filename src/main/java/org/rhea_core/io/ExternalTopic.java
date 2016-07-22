package org.rhea_core.io;

import org.rhea_core.Stream;
import org.rhea_core.internal.expressions.creation.FromSource;
import org.rhea_core.internal.output.MultipleOutput;
import org.rhea_core.internal.output.Output;
import org.rhea_core.internal.output.SinkOutput;
import org.rhea_core.serialization.SerializationStrategy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Orestis Melkonian
 */
public abstract class ExternalTopic<T, C> implements Source<T>, Sink<T> {

    protected String name;
    protected C client;

    public abstract void setClient(C client);

    protected ExternalTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String name() {
        return name + "[" + Thread.currentThread().getId() + "]";
    }

    /**
     * Extracts all {@link ExternalTopic}s involved in the given dataflow graph.
     * @param stream the {@link Stream} defined by the user
     * @param output the {@link Output} to re-direct the evaluated result
     * @return all {@link ExternalTopic}s as a {@link List}
     */
    public static List<ExternalTopic> extractAll(Stream stream, Output output) {
        List<ExternalTopic> topics = stream.getGraph()
                .vertexSet()
                .stream()
                .filter(n -> n instanceof FromSource)
                .map(n ->  ((FromSource) n).getSource())
                .filter(s -> s instanceof ExternalTopic)
                .map(s -> ((ExternalTopic) s))
                .collect(Collectors.toList());

        if (output instanceof MultipleOutput) {
            for (Output out : ((MultipleOutput) output).getOutputs())
                if ((out instanceof SinkOutput) && (((SinkOutput) out).getSink() instanceof ExternalTopic))
                    topics.add((ExternalTopic) ((SinkOutput) out).getSink());
        }
        else if ((output instanceof SinkOutput) && (((SinkOutput) output).getSink() instanceof ExternalTopic))
            topics.add((ExternalTopic) ((SinkOutput) output).getSink());

        return topics;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null)
                && (obj instanceof ExternalTopic)
                && (obj.getClass() == getClass())
                && name.equals(((ExternalTopic) obj).getName());
    }
}
