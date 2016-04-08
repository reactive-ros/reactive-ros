package org.rhea_core.distribution.graph;

import org.jgrapht.graph.DirectedPseudograph;
import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.graph.FlowGraph;
import org.rhea_core.io.InternalTopic;
import org.rhea_core.util.functions.Func0;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Orestis Melkonian
 */
public class DistributedGraph extends DirectedPseudograph<Transformer, TopicEdge> {
    public Transformer toConnect;

    public DistributedGraph(FlowGraph graph, Func0<InternalTopic> topicGenerator) {
        super(TopicEdge.class);
        toConnect = graph.getConnectNode();
        graph.vertexSet().stream().forEach(this::addVertex);
        graph.edgeSet().stream().forEach(e -> this.addEdge(e.getSource(), e.getTarget(), new TopicEdge(e, topicGenerator.call())));
    }

    public List<Transformer> getRoots() {
        return vertexSet().stream().filter(p -> p instanceof NoInputExpr).collect(Collectors.toList());
    }
}
