package org.rhea_core.internal.expressions.utility;

import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.notifications.Notification;

/**
 * @author Orestis Melkonian
 */
public class DematerializeExpr<T> extends SingleInputExpr<Notification<T>> implements Transformer<T> {
    @Override
    public Transformer<T> clone() {
        return new DematerializeExpr<>();
    }
}
