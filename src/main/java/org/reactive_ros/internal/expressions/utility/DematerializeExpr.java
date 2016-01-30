package org.reactive_ros.internal.expressions.utility;

import org.reactive_ros.internal.expressions.SingleInputExpr;
import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.notifications.Notification;

/**
 * @author Orestis Melkonian
 */
public class DematerializeExpr<T> extends SingleInputExpr<Notification<T>> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new DematerializeExpr<>();
    }
}
