package org.rhea_core.distribution;

import org.rhea_core.evaluation.EvaluationStrategy;
import org.rhea_core.util.functions.Func0;

/**
 * @author Orestis Melkonian
 */
public abstract class DistributedEvaluationStrategy implements EvaluationStrategy {
    private Func0<EvaluationStrategy> strategyGenerator;
}
