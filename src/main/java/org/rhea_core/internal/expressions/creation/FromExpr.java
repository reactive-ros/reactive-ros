package org.rhea_core.internal.expressions.creation;

import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.Transformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Orestis Melkonian
 */
public class FromExpr<T> extends NoInputExpr implements Transformer<T> {
    private Iterable<? extends T> collection;

    public FromExpr(Iterable<? extends T> collection) {
        this.collection = collection;
    }

    public Iterable<? extends T> getCollection() {
        return collection;
    }

    public void setCollection(List<? extends T> collection) {
        this.collection = collection;
    }

    @Override
    public Transformer<T> clone() {
        /*List<T> copied = new ArrayList<>();
        collection.iterator().forEachRemaining(copied::add);
        return new FromExpr<>(copied);*/
        return new FromExpr<>(collection);
    }

    @Override
    public String toString() {
        return "From: " + collection.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof FromExpr))
            return false;

        Iterator<? extends T> it1 = collection.iterator();
        Iterator<? extends T> it2 = ((FromExpr<T>) obj).getCollection().iterator();
        while (it1.hasNext() && it2.hasNext())
            if (!it1.next().equals(it2.next()))
                return false;
        return !it1.hasNext() && !it2.hasNext();
    }
}
