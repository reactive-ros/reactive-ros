package org.rhea_core.optimization;

import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.combining.ConcatMultiExpr;
import org.rhea_core.internal.expressions.combining.MergeMultiExpr;
import org.rhea_core.internal.expressions.feedback.EntryPointExpr;
import org.rhea_core.internal.graph.FlowGraph;
import org.rhea_core.internal.graph.SimpleEdge;

/**
 * @author Orestis Melkonian
 */
public class EntryRemoval implements Optimizer {

    @Override
    public void optimize(FlowGraph graph) {
        for (SimpleEdge edge : graph.edgeSet()) {
            Transformer source = edge.getSource();
            Transformer target = edge.getTarget();

            // concat|merge -> entry
            if ((source instanceof ConcatMultiExpr || source instanceof MergeMultiExpr)
            &&  target instanceof EntryPointExpr) {
                graph.removeEdge(source, target);
                for (Transformer succ : graph.successors(target))
                    graph.addEdge(source, succ);
                graph.removeVertex(target);
            }
        }
    }
}
