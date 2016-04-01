package org.rhea_core.optimization;

import org.rhea_core.internal.graph.FlowGraph;
import org.rhea_core.optimization.optimizers.NodeMerger;
import org.rhea_core.optimization.optimizers.ProactiveFiltering;

/**
 * @author Orestis Melkonian
 */
public class DefaultOptimizationStrategy implements OptimizationStrategy {
    int desiredGranularity;

    public DefaultOptimizationStrategy(int desiredGranularity) {
        this.desiredGranularity = desiredGranularity;
    }

    @Override
    public void optimize(FlowGraph graph) {
        new ProactiveFiltering().optimize(graph);
        new NodeMerger(desiredGranularity).optimize(graph);
    }
}
