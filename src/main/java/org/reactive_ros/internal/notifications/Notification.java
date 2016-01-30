package org.reactive_ros.internal.notifications;

import org.reactive_ros.Stream;

/**
 * A class representing notifications sent to a {@link Stream}.
 * @author Orestis Melkonian
 */
public final class Notification<T> {
    private final Kind kind;
    private final Throwable throwable;
    private final T value;

    private static final Notification<Void> ON_COMPLETED = new Notification<Void>(Kind.OnCompleted, null, null);

    /**
     * Creates and returns a {@code Notification} of variety {@code Kind.OnNext}, and assigns it a value.
     * @param t the item to assign to the notification as its value
     * @return an {@code OnNext} variety of {@code Notification}
     */
    public static <T> Notification<T> createOnNext(T t) {
        return new Notification<T>(Kind.OnNext, t, null);
    }

    /**
     * Creates and returns a {@code Notification} of variety {@code Kind.OnError}, and assigns it an exception.
     * @param e the exception to assign to the notification
     * @return an {@code OnError} variety of {@code Notification}
     */
    public static <T> Notification<T> createOnError(Throwable e) {
        return new Notification<T>(Kind.OnError, null, e);
    }

    /**
     * Creates and returns a {@code Notification} of variety {@code Kind.OnCompleted}.
     * @return an {@code OnCompleted} variety of {@code Notification}
     */
    @SuppressWarnings("unchecked")
    public static <T> Notification<T> createOnCompleted() {
        return (Notification<T>) ON_COMPLETED;
    }

    private Notification(Kind kind, T value, Throwable e) {
        this.value = value;
        this.throwable = e;
        this.kind = kind;
    }

    /**
     * Retrieves the exception associated with this (onError) notification.
     *
     * @return the Throwable associated with this (onError) notification
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * Retrieves the item associated with this (onNext) notification.
     *
     * @return the item associated with this (onNext) notification
     */
    public T getValue() {
        return value;
    }

    /**
     * Retrieves the kind of this notification: {@code OnNext}, {@code OnError}, or {@code OnCompleted}
     * @return the kind of the notification: {@code OnNext}, {@code OnError}, or {@code OnCompleted}
     */
    public Kind getKind() {
        return kind;
    }


    public enum Kind {
        OnNext, OnError, OnCompleted
    }
}
