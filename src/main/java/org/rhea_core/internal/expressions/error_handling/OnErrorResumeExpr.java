package org.rhea_core.internal.expressions.error_handling;

import org.rhea_core.Stream;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.SingleInputExpr;

/**
 * @author Orestis Melkonian
 */
public class OnErrorResumeExpr<T> extends SingleInputExpr implements Transformer<T> {
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
