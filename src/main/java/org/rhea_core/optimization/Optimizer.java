package org.rhea_core.optimization;

import org.rhea_core.internal.graph.FlowGraph;

/**
 * @author Orestis Melkonian
 */
public interface Optimizer {
    void optimize(FlowGraph graph);
}
