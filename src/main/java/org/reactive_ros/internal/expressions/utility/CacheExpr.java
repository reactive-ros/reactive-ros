package org.reactive_ros.internal.expressions.utility;

import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.internal.expressions.Transformer;

/**
 * @author Orestis Melkonian
 */
public class CacheExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new CacheExpr<>();
    }
}
