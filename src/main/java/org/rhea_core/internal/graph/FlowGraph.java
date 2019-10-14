package org.rhea_core.internal.graph;

import org.jgrapht.EdgeFactory;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DirectedPseudograph;
import org.rhea_core.Stream;
import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.feedback.EntryPointExpr;
import org.rhea_core.util.functions.Func0;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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

    public static FlowGraph merge(Stream... streams) {
        FlowGraph graph = new FlowGraph();
        for (Stream s : streams) {
            Graphs.addAllVertices(graph, s.getGraph().vertexSet());
            Graphs.addAllEdges(graph, s.getGraph(), s.getGraph().edgeSet());
            graph.addConnectNode(s.getToConnect());
        }

        return graph;
    }

    public void setConnectNode(Transformer toConnect) {
        this.toConnect = toConnect;
    }

    public void setConnectNodes(List<Transformer> toConnect) {
        toConnectMulti = toConnect;
        toConnectMulti.forEach(this::addVertex);
    }

    public void addConnectNode(Transformer toConnect) {
        toConnectMulti.add(toConnect);
    }

    public Transformer getConnectNode() {
        return toConnect;
    }

    public void addConnectVertex(Transformer expr) {
        addVertex(expr);
        setConnectNode(expr);
    }

    public Transformer getEntryPoint() {
        return vertexSet().stream().filter(n -> n instanceof EntryPointExpr).collect(Collectors.toList()).get(0);
    }

    // Attach output node to given vertex of the current graph
    public void attach(Transformer newConnect, Transformer toConnect) {
        assert vertices().contains(toConnect);

        addVertex(newConnect);
        addEdge(toConnect, newConnect);
        setConnectNode(newConnect);
    }

    public void attachMulti(Transformer newConnect) {
        addVertex(newConnect);
        if (toConnectMulti.isEmpty())
            addEdge(this.toConnect, newConnect);
        for (Transformer p : toConnectMulti)
            addEdge(p, newConnect);

        setConnectNode(newConnect);
        toConnectMulti.clear();
    }

    public List<Transformer> getRoots() {
        return vertexSet().stream().filter(p -> p instanceof NoInputExpr).collect(Collectors.toList());
    }

    public FlowGraph copy() {
        FlowGraph ret = new FlowGraph();
        Map<Transformer, Transformer> mapper = new HashMap<>();
        vertexSet().stream().forEach(p -> {
            Transformer cl = p.clone();
            mapper.put(p, cl);
            ret.addVertex(cl);
        });
        edgeSet().stream().forEach(e -> {
            Transformer source = mapper.get(e.getSource());
            Transformer target = mapper.get(e.getTarget());
            ret.addEdge(source, target, new SimpleEdge(source, target));
        });
        if (toConnectMulti.isEmpty())
            ret.addConnectVertex(mapper.get(getConnectNode()));
        else
            ret.setConnectNodes(toConnectMulti.stream().map(mapper::get).collect(Collectors.toList()));
        return ret;
    }

    public Transformer predecessor(Transformer target) {
        return predecessors(target).get(0);
    }

    public Transformer successor(Transformer target) {
        return successors(target).get(0);
    }

    public List<Transformer> predecessors(Transformer target) {
        return edgeSet().stream()
                .filter(e -> e.getTarget().equals(target))
                .sorted((e1, e2) -> (e1.getOrder() < e2.getOrder()) ? -1 : e1.getOrder() > e2.getOrder() ? 1 : 0)
                .map(SimpleEdge::getSource)
                .collect(Collectors.toList());
    }

    public List<Transformer> successors(Transformer source) {
        return edgeSet().stream()
                .filter(e -> e.getSource().equals(source))
                .sorted((e1, e2) -> (e1.getOrder() < e2.getOrder()) ? -1 : e1.getOrder() > e2.getOrder() ? 1 : 0)
                .map(SimpleEdge::getTarget)
                .collect(Collectors.toList());
    }

    public boolean singular(Transformer source) {
        return successors(source).size() == 1;
    }

    public void reorder(Transformer source, Transformer target) {
        SimpleEdge e = getEdge(source, target);

        if ((e == null) || (successors(source).size() != 1)) // cannot reorder when multiple connections exist
            throw new RuntimeException("Cannot reorder. More predecessors than 1.");

        removeEdge(e);

        for (Transformer predecessor : predecessors(source)) {
            removeEdge(predecessor, source);
            addEdge(predecessor, target);
        }

        for (Transformer successor : successors(target)) {
            removeEdge(target, successor);
            addEdge(source, successor);
        }

        addEdge(target, source);

        if (toConnect.equals(target))
            setConnectNode(source);
    }

    public void reorder(Transformer source, Transformer target, Func0<Transformer> sourceGen, Func0<Transformer> targetGen) {
        SimpleEdge e = getEdge(source, target);

        if ((e == null) || (successors(source).size() != 1)) // cannot reorder when multiple connections exist
            return;

        Transformer newSource = sourceGen.call();
        Transformer newTarget = targetGen.call();
        addVertex(newSource);
        addVertex(newTarget);
        addEdge(newTarget, newSource);

        Transformer predecessor = predecessor(source);
        addEdge(predecessor, newTarget);

        for (Transformer successor : successors(target))
            addEdge(newSource, successor);

        if (toConnect.equals(target))
            setConnectNode(newSource);

        removeVertex(source);
        removeVertex(target);
    }

    public void merge(Transformer source, Transformer target, Transformer merged) {
        addVertex(merged);

        for (Transformer pred : predecessors(source))
            addEdge(pred, merged);

        for (Transformer succ : successors(target))
            addEdge(merged, succ);

        if (toConnect.equals(target))
            setConnectNode(merged);

        removeVertex(source);
        removeVertex(target);
    }

    public int size() {
        return (int) vertexSet().stream().count();
    }

    public List<SimpleEdge> edges() {
        List<SimpleEdge> ret = new ArrayList<>();
        edgeSet().forEach(ret::add);
        return ret;
    }

    public List<Transformer> vertices() {
        List<Transformer> ret = new ArrayList<>();
        vertexSet().forEach(ret::add);
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof FlowGraph))
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
        return copy();
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
