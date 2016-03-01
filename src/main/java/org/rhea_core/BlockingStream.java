package org.rhea_core;

import org.rhea_core.util.functions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Orestis Melkonian
 */
public class BlockingStream<T> {
    private Stream<T> s;

    private BlockingStream(Stream<T> s) {
        this.s = s;
    }

    public static <T> BlockingStream<T> from(Stream<T> s) {
        return new BlockingStream<>(s);
    }

    public T elementAt(int index) {
        s = s.elementAt(index);
        AtomicReference<T> ret = new AtomicReference<>();
        subscribe(ret::set);
        return ret.get();
    }

    public T first() {
        return elementAt(0);
    }

    public T last() {
        s = s.last();
        return first();
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>();
        subscribe(list::add);
        return list;
    }

    public Queue<T> toQueue() {
        Queue<T> queue = new ConcurrentLinkedDeque<>();
        subscribe(queue::add);
        return queue;
    }


    /**
     * Blocking subscribe
     */

    public void subscribe() {
        final Semaphore semaphore = new Semaphore(0);

        s.subscribe(Actions.EMPTY, Actions.EMPTY, semaphore::release);

        await(semaphore);
    }

    public void subscribe(Action1<? super T> action) {
        final Semaphore semaphore = new Semaphore(0);

        s.subscribe(action, Actions.EMPTY, semaphore::release);

        await(semaphore);
    }

    public void subscribe(Action1<T> action, Action1<Throwable> error) {
        final Semaphore semaphore = new Semaphore(0);

        s.subscribe(action, error, semaphore::release);

        await(semaphore);
    }

    public void subscribe(Action1<T> action, Action1<Throwable> error, Action0 complete) {
        final Semaphore semaphore = new Semaphore(0);

        s.subscribe(action, error, () -> {
            complete.call();
            semaphore.release();
        });

        await(semaphore);
    }

    private void await(Semaphore semaphore) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
