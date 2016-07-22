package org.rhea_core.distribution;

import org.rhea_core.Stream;
import org.rhea_core.evaluation.EvaluationStrategy;
import org.rhea_core.internal.output.Output;

/**
 * @author Orestis Melkonian
 */
public class SingleMachineDistributionStrategy implements DistributionStrategy {

    private final EvaluationStrategy evaluationStrategy;

    public SingleMachineDistributionStrategy(EvaluationStrategy evaluationStrategy) {
        this.evaluationStrategy = evaluationStrategy;
    }

    @Override
    public void distribute(Stream stream, Output output) {
        evaluationStrategy.evaluate(stream, output);
    }
}
