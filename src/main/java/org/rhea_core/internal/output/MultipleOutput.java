package org.rhea_core.internal.output;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.util.functions.Action;

import java.util.List;

/**
 * Applies an {@link Action} to a {@link Transformer}'s resulting stream.
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
