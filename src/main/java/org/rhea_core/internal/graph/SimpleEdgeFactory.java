package org.rhea_core.internal.graph;

import org.rhea_core.internal.expressions.Transformer;
import org.jgrapht.EdgeFactory;

import java.io.Serializable;

/**
 * @author Orestis Melkonian
 */
public class SimpleEdgeFactory implements EdgeFactory<Transformer, SimpleEdge>, Serializable {
    @Override
    public SimpleEdge createEdge(Transformer sourceVertex, Transformer targetVertex) {
        return new SimpleEdge(sourceVertex, targetVertex);
    }
}
