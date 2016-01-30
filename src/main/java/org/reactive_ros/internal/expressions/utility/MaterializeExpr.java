package org.reactive_ros.internal.expressions.utility;

import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.notifications.Notification;

/**
 * @author Orestis Melkonian
 */
public class MaterializeExpr<T> extends SingleInputExpr<T> implements Transformer<Notification<T>> {
    @Override
    public Transformer<Notification<T>> clone() {
        return new MaterializeExpr<>();
    }
}
