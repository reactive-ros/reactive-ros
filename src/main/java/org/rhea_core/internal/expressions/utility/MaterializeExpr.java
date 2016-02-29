package org.rhea_core.internal.expressions.utility;

import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.notifications.Notification;

/**
 * @author Orestis Melkonian
 */
public class MaterializeExpr<T> extends SingleInputExpr<T> implements Transformer<Notification<T>> {
    @Override
    public Transformer<Notification<T>> clone() {
        return new MaterializeExpr<>();
    }
}
