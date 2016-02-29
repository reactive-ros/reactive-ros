package org.rhea_core.internal.expressions;

import java.io.Serializable;

/**
 * All components of the Intermediate Representation implement this interface.
 * @author Orestis Melkonian
 */
public interface Transformer<T> extends Serializable {
    Transformer clone();
}
