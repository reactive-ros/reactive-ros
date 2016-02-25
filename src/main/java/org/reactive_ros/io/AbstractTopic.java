package org.reactive_ros.io;

import org.reactive_ros.evaluation.Serializer;

/**
 * @author Orestis Melkonian
 */
public abstract class AbstractTopic<T, B> implements Source<T>, Sink<T> {

    protected String name;
    protected Serializer<B> serializer;

    protected AbstractTopic(String name, Serializer<B> serializer) {
        this.name = name;
        this.serializer = serializer;
    }

    public String getName() {
        return name;
    }

    public String name() {
        return name + "[" + Thread.currentThread().getId() + "]";
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof AbstractTopic
                && obj.getClass() == this.getClass()
                && name.equals(((AbstractTopic) obj).getName());
    }
}
