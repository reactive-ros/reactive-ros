package org.reactive_ros.internal.graph;

import org.reactive_ros.Stream;
import org.reactive_ros.internal.expressions.NoInputExpr;
import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.feedback.EntryPointExpr;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Orestis Melkonian
 */
public class FlowGraph extends DirectedPseudograph<Transformer, SimpleEdge> {
    private Transformer toConnect;
    private List<Transformer> toConnectMulti = new ArrayList<>();

    private static EdgeFactory<Transformer, SimpleEdge> ef = new SimpleEdgeFactory();

    public FlowGraph() {
        super(ef);
    }

    public FlowGraph(FlowGraph other) {
        super(ef);
        setConnectNode(other.getConnectNode());
        Graphs.addAllVertices(this, other.vertexSet());
        Graphs.addAllEdges(this, other, other.edgeSet());
    }

    public FlowGraph(List<Stream> streams, List<FlowGraph> graphs) {
        super(ef);
        toConnectMulti.clear();
        // Copy nodes
        for (FlowGraph graph : graphs)
            Graphs.addAllVertices(this, graph.vertexSet());
        // Copy edges
        for (FlowGraph graph : graphs)
            Graphs.addAllEdges(this, graph, graph.edgeSet());
        // Set ConnectNodes
        for (Stream stream : streams)
            toConnectMulti.add(stream.getToConnect());
    }

    public void setConnectNode(Transformer toConnect) {
        this.toConnect = toConnect;
    }

    public void setConnectNodes(List<Transformer> toConnect) {
        toConnectMulti = toConnect;
        toConnectMulti.forEach(this::addVertex);
    }

    public Transformer getConnectNode() {
        return toConnect;
    }

    public Transformer getEntryPoint() {
        return vertexSet().stream().filter(n -> n instanceof EntryPointExpr).collect(Collectors.toList()).get(0);
    }

    public void addConnectVertex(Transformer expr) {
        addVertex(expr);
        setConnectNode(expr);
    }

    public void attach(Transformer newConnect) {
        addVertex(newConnect);
        addEdge(toConnect, newConnect);
        setConnectNode(newConnect);
    }

    public void attachMulti(Transformer newConnect) {
        addVertex(newConnect);
        for (Transformer p : toConnectMulti)
            addEdge(p, newConnect);
        setConnectNode(newConnect);
//        toConnectMulti.clear();
    }

    public List<Transformer> getRoots() {
        return vertexSet().stream().filter(p -> p instanceof NoInputExpr).collect(Collectors.toList());
    }

    public FlowGraph copy() {
        FlowGraph ret = new FlowGraph();
        Map<Transformer, Transformer> mapper = new HashMap<>();
        this.vertexSet().stream().forEach(p -> {
            Transformer cl = p.clone();
            mapper.put(p, cl);
            ret.addVertex(cl);
        });
        this.edgeSet().stream().forEach(e -> {
            Transformer source = mapper.get(e.getSource());
            Transformer target = mapper.get(e.getTarget());
            ret.addEdge(source, target, new SimpleEdge(source, target));
        });
        if (this.toConnectMulti.isEmpty())
            ret.addConnectVertex(mapper.get(this.getConnectNode()));
        else
            ret.setConnectNodes(this.toConnectMulti.stream().map(mapper::get).collect(Collectors.toList()));
        return ret;
    }

    public List<Transformer> predecessors(Transformer target) {
        return edgeSet().stream()
                .filter(e -> e.getTarget().equals(target))
                .sorted((e1, e2) -> e1.getOrder() < e2.getOrder() ? -1 : e1.getOrder() > e2.getOrder() ? 1 : 0)
                .map(SimpleEdge::getSource)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FlowGraph))
            return false;
        FlowGraph other = (FlowGraph) obj;

        // Compare all nodes
        Queue<Transformer> q = new ArrayDeque<>(other.vertexSet());
        for (Transformer p : vertexSet())
            if (!p.equals(q.remove())) return false;

        // Compare all edges
        Queue<SimpleEdge> q2 = new ArrayDeque<>(other.edgeSet());
        for (SimpleEdge e : edgeSet())
            if (!e.equals(q2.remove())) return false;

        return true;
    }

    @Override
    public FlowGraph clone() {
        return this.copy();
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "\nVertices: ";
        for (Transformer p : vertexSet()) ret += "\n\t" + p;
        ret += "\nEdges: ";
        for (SimpleEdge e : edgeSet()) ret += "\n\t" + e.getSource() + " <=> " + e.getTarget();
        return ret;
    }

    public String toSimpleString() {
        String ret = "";
        ret += "V { ";
        for (Transformer p : vertexSet()) ret += "," + p;
        ret += "} E { ";
        for (SimpleEdge e : edgeSet()) ret += "," + e.getSource() + " <=> " + e.getTarget();
        ret += " }";
        return ret;
    }

    public String predecessors() {
        String ret = "";
        for (Transformer t : vertexSet()) {
            List<Transformer> pred = predecessors(t);
            ret += t + " <= " + pred + "\n";
        }
        return ret;
    }
}