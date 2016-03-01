package org.rhea_core.optimization;

import org.rhea_core.Stream;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.transformational.MapExpr;
import org.rhea_core.internal.graph.FlowGraph;
import org.rhea_core.internal.graph.SimpleEdge;

/**
 * @author Orestis Melkonian
 */
public class Optimizer {

    public static Stream optimize(Stream init, int granularity) {
        FlowGraph graph = init.getGraph();
        int mergeNo = graph.size() - granularity;
        for (int i = 0; i < mergeNo; i++) {
            FlowGraph next = merge(graph);
            if (next == null)
                return new Stream(graph);
            graph = next;
        }
        return init;
    }

    public static FlowGraph merge(FlowGraph prev) {
        for (SimpleEdge edge : prev.edgeSet()) {
            Transformer t1 = edge.getSource();
            Transformer t2 = edge.getSource();

            if (merge(t1, t2, prev))
                return prev;
        }

        return null;
    }

    private static boolean merge(Transformer t1, Transformer t2, FlowGraph graph) {
        boolean changed = false;
        Transformer merged;
        if (t1 instanceof MapExpr && t2 instanceof MapExpr) {
            merged = new MapExpr(i -> ((MapExpr) t2).getMapper().call(((MapExpr) t1).getMapper().call(i)));
            changed = true;
        }
        if (changed) {
            // Update edges & nodes
        }
        return changed;
    }
}
