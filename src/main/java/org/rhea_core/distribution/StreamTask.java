package org.rhea_core.distribution;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import org.rhea_core.Stream;
import org.rhea_core.evaluation.EvaluationStrategy;
import org.rhea_core.internal.output.Output;
import org.rhea_core.internal.output.SinkOutput;
import org.rhea_core.io.Sink;
import org.rhea_core.util.functions.Func0;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class StreamTask implements Runnable, Serializable, HazelcastInstanceAware {

    private HazelcastInstance hazelcastInstance;

    protected Func0<EvaluationStrategy> strategyGenerator;
    protected Stream stream;
    protected Output output;
    private List<String> requiredAttributes;

    public StreamTask(Func0<EvaluationStrategy> strategyGenerator, Stream stream, Output output) {
        this.strategyGenerator = strategyGenerator;
        this.stream = stream;
        this.output = output;
        requiredAttributes = new ArrayList<>();
    }

    public StreamTask(Func0<EvaluationStrategy> strategyGenerator, Stream stream, Output output, List<String> requiredAttributes) {
        this.strategyGenerator = strategyGenerator;
        this.stream = stream;
        this.output = output;
        this.requiredAttributes = requiredAttributes;
    }

    public List<String> getRequiredAttributes() {
        return requiredAttributes;
    }

    public Output getOutput() {
        return output;
    }

    public Stream getStream() {
        return stream;
    }

    public Func0<EvaluationStrategy> getStrategyGenerator() {
        return strategyGenerator;
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    @Override
    public void run() {
        System.out.print(instanceIP(hazelcastInstance) + ": ");
        strategyGenerator.call().evaluate(stream, output);
    }

    @Override
    public String toString() {
        return "\n\n======================== "
                + ManagementFactory.getRuntimeMXBean().getName() + "@" + Thread.currentThread().getId()
                + " ========================"
                + "\n" + stream.getGraph()
                + "\n\t===>\t" + output + "\n"
                + "\n==================================================\"\n\n";
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    private String instanceIP(HazelcastInstance instance) {
        String str = instance.toString();
        return (str.substring(str.indexOf("Address") + 7, str.length() - 1)).replace("[","").replace("]","");
    }

    /**
     * MERGING: This stream has a topic output that the other stream has as input.
     */
    public boolean canMergeWith(StreamTask other) {
        // Evaluation strategies must be the same
        EvaluationStrategy strategy1 = strategyGenerator.call();
        EvaluationStrategy strategy2 = other.getStrategyGenerator().call();
        if (!strategy1.equals(strategy2)) // TODO implement equals
            return false;

        // This stream must have a single topic output
        if (!(output instanceof SinkOutput))
            return false;
        Sink sink = ((SinkOutput) output).getSink();
        // TODO sink

        return false;
    }

    public StreamTask merge(StreamTask other) {
        // Merge streams
        Stream newStream = null;

        // Output is the other stream's output
        Output newOutput = other.getOutput();

        // Inherits both children's required attributes
        List<String> attr = new ArrayList<>(requiredAttributes);
        attr.addAll(other.requiredAttributes);

        return new StreamTask(strategyGenerator, newStream, newOutput, attr);
    }
}
