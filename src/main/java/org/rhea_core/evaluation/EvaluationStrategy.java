package org.rhea_core.evaluation;

import org.rhea_core.Stream;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.graph.FlowGraph;
import org.rhea_core.internal.output.Output;

import java.io.Serializable;

/**
 * Evaluates a dataflow graph as {@link FlowGraph} containing any topology of {@link Transformer}.
 * @author Orestis Melkonian
 */
public interface EvaluationStrategy extends Serializable {
    <T> void evaluate(Stream<T> stream, Output output);
}
