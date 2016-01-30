package org.reactive_ros.internal.output;

import org.reactive_ros.internal.expressions.Transformer;

import java.util.List;

/**
 * Applies an {@link org.reactive_ros.util.functions.Action} to a {@link Transformer}'s resulting stream.
 * @author Orestis Melknonian
 */
public class MultipleOutput implements Output {
    private List<Output> outputs;

    public MultipleOutput(List<Output> outputs) {
        this.outputs = outputs;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    @Override
    public String toString() {
        return "Multi: " + outputs.toString();
    }
}
