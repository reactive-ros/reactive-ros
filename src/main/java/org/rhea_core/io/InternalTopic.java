package org.rhea_core.io;

import org.rhea_core.serialization.SerializationStrategy;

/**
 * @author Orestis Melkonian
 */
public abstract class InternalTopic<T, C> extends ExternalTopic<T, C> {

    protected SerializationStrategy serializationStrategy;

    protected InternalTopic(String name, SerializationStrategy serializationStrategy) {
        super(name);
        this.serializationStrategy = serializationStrategy;
    }
}
