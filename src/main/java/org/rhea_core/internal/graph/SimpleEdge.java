package org.rhea_core.internal.graph;

import org.rhea_core.internal.expressions.MultipleInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.jgrapht.graph.DefaultEdge;

/**
 * @author Orestis Melkonian
 */
public class SimpleEdge extends DefaultEdge {
    private Transformer source;
    private Transformer target;

    private int order;

    public SimpleEdge(Transformer source, Transformer target) {
        this.target = target;
        this.source = source;
        order = (target instanceof MultipleInputExpr) ? ((MultipleInputExpr) target).orderRef++ : 0;
    }

    public SimpleEdge(Transformer source, Transformer target, int order) {
        this.target = target;
        this.source = source;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }


    @Override
    public Transformer getSource() {
        return source;
    }

    @Override
    public Transformer getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof SimpleEdge
                && source.equals(((SimpleEdge) obj).getSource())
                && target.equals(((SimpleEdge) obj).getTarget());
    }

    @Override
    public SimpleEdge clone() {
        return new SimpleEdge(source.clone(), target.clone(), order);
    }

    @Override
    public String toString() {
        return "";
/*        return "#" + order;
        return source + " <=> " + target;*/
    }
}
