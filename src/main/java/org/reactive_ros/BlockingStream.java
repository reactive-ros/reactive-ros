package org.reactive_ros;

import org.reactive_ros.util.functions.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
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

    public T first() {
        return elementAt(0);
    }

    public T last() {
        return s.last().toBlocking().first();
    }

    public T elementAt(int index) {
        final AtomicReference<T> ret = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);

        s.elementAt(index).subscribe(t -> {
            ret.set(t);
            latch.countDown();
        });

        await(latch);
        return ret.get();
    }

    public <R> R collect(Func0<R> stateFactory, Action2<R, ? super T> collector) {
        return s.collect(stateFactory, collector).toBlocking().first();
    }

    public List<T> toList() {
        return s.toList().toBlocking().first();
    }

    public Queue<T> toQueue() {
        Queue<T> queue = new LinkedList<>();
        s.toBlocking().subscribe(queue::add);
        return queue;
    }

    public List<T> toSortedList(Func2<? super T, ? super T, Integer> comparator) {
        return s.toSortedList(comparator).toBlocking().first();
    }

    public <K> Map<K,T> toMap(Func1<? super T, ? extends K> keySelector) {
        return s.toMap(keySelector).toBlocking().first();
    }
    public <K, V> Map<K,V> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        return s.toMap(keySelector, valueSelector).toBlocking().first();
    }
    public <K, V> Map<K,V> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<Map<K, V>> mapFactory) {
        return s.toMap(keySelector, valueSelector, mapFactory).toBlocking().first();
    }


    public void subscribe() {
        final CountDownLatch latch = new CountDownLatch(1);

        s.subscribe(Actions.EMPTY, Actions.EMPTY, latch::countDown);

        await(latch);
    }

    public void subscribe(Action1<? super T> action) {
        final CountDownLatch latch = new CountDownLatch(1);

        s.subscribe(action, Actions.EMPTY, latch::countDown);

        await(latch);
    }

    public void subscribe(Action1<T> action, Action1<Throwable> error) {
        final CountDownLatch latch = new CountDownLatch(1);

        s.subscribe(action, error, latch::countDown);

        await(latch);
    }

    public void subscribe(Action1<T> action, Action1<Throwable> error, Action0 complete) {
        final CountDownLatch latch = new CountDownLatch(1);

        s.subscribe(action, error, () -> {
            complete.call();
            latch.countDown();
        });

        await(latch);
    }

    private void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
