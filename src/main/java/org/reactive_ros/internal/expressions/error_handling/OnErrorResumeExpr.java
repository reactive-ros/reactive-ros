package org.reactive_ros.internal.expressions.error_handling;

import org.reactive_ros.Stream;
import org.reactive_ros.internal.expressions.Transformer;
import org.reactive_ros.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class OnErrorResumeExpr<T> extends SingleInputExpr<T> implements Transformer<T> {
    private Stream<? extends T> resumeStream;

    public OnErrorResumeExpr(Stream<? extends T> resumeStream) {
        this.resumeStream = resumeStream;
    }

    public Stream<? extends T> getResumeStream() {
        return resumeStream;
    }

    @Override
    public Transformer<T> clone() {
        return new OnErrorResumeExpr<>(resumeStream.clone());
    }

    @Override
    public String toString() {
        return "onErrorResume: " + resumeStream;
    }
}
