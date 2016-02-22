package org.reactive_ros.distribution;

import org.reactive_ros.Stream;
import org.reactive_ros.evaluation.EvaluationStrategy;
import org.reactive_ros.internal.output.Output;
import org.reactive_ros.util.functions.Func0;

import java.io.Serializable;

/**
 * @author Orestis Melkonian
 */
public class StreamTask implements Runnable, Serializable {

    private Func0<EvaluationStrategy> strategyGenerator;
    private Stream stream;
    private Output output;

    public StreamTask(Func0<EvaluationStrategy> strategyGenerator, Stream stream, Output output) {
        this.strategyGenerator = strategyGenerator;
        this.stream = stream;
        this.output = output;
    }

    @Override
    public void run() {
        strategyGenerator.call().evaluate(stream, output);
    }

    @Override
    public String toString() {
        return "\t" + strategyGenerator.toString()
                + "\n\t" + stream.toString()
                + "\n\t" + output.toString();
    }
}
