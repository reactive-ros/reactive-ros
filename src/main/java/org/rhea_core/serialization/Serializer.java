package org.rhea_core.serialization;

import org.rhea_core.evaluation.EvaluationStrategy;
import org.rhea_core.internal.Notification;

import java.io.Serializable;

/**
 * All (de)serializers used by any {@link EvaluationStrategy} should implement this interface.
 * @param <B> the cross-machine representation of bytes
 * @author Orestis Melkonian
 */
public interface Serializer<B> extends Serializable {

    /**
     * Serializes given {@link Object}.
     * @param obj the {@link Object} to serialize
     * @return an array of bytes
     */
    <T> B serialize(Notification<T> obj);

    /**
     * Deserializes given array of bytes.
     * @param bytes the array of bytes to deserialize
     * @return the {@link Object} result from the deserialization
     */
    <T> Notification<T> deserialize(B bytes);
}
