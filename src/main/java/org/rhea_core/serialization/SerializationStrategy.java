package org.rhea_core.serialization;

import org.rhea_core.evaluation.EvaluationStrategy;
import org.rhea_core.internal.Notification;

import java.io.Serializable;

/**
 * All (de)serializers used by any {@link EvaluationStrategy} should implement this interface.
 * @author Orestis Melkonian
 */
public interface SerializationStrategy extends Serializable {

    /**
     * Serializes given {@link Object}.
     * @param obj the {@link Object} to serialize
     * @return the object serialized
     */
    <T> byte[] serialize(Notification<T> obj);

    /**
     * Deserializes given array of bytes.
     * @param bytes the array of bytes to deserialize
     * @return the {@link Object} result from the deserialization
     */
    Notification deserialize(byte[] bytes);
}
