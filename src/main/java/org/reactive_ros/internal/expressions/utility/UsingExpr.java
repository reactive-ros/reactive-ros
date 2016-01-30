package org.reactive_ros.internal.expressions.utility;

import org.reactive_ros.Stream;
import org.reactive_ros.internal.expressions.NoInputExpr;
import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.util.functions.Action1;
import org.reactive_ros.util.functions.Func0;
import org.reactive_ros.util.functions.Func1;

/**
 * @author Orestis Melkonian
 */
public class UsingExpr<T, Resource> extends NoInputExpr<T> implements Transformer<T> {
    private Func0<Resource> resourceFactory;
    private Func1<? super Resource,? extends Stream<? extends T>> streamFactory;
    private Action1<? super Resource> disposeAction;

    public UsingExpr(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Stream<? extends T>> streamFactory, Action1<? super Resource> disposeAction) {
        this.resourceFactory = resourceFactory;
        this.streamFactory = streamFactory;
        this.disposeAction = disposeAction;
    }

    public Func0<Resource> getResourceFactory() {
        return resourceFactory;
    }

    public Func1<? super Resource, ? extends Stream<? extends T>> getStreamFactory() {
        return streamFactory;
    }

    public Action1<? super Resource> getDisposeAction() {
        return disposeAction;
    }

    @Override
    public Transformer<T> clone() {
        return new UsingExpr<T, Resource>(resourceFactory::call, streamFactory::call, disposeAction::call);
    }
}
