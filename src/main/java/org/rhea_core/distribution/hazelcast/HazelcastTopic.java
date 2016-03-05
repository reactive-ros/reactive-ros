package org.rhea_core.distribution.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.rhea_core.Stream;
import org.rhea_core.evaluation.DefaultSerializer;
import org.rhea_core.internal.notifications.Notification;
import org.rhea_core.internal.output.Output;
import org.rhea_core.io.AbstractTopic;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Orestis Melkonian
 */
public class HazelcastTopic<T> extends AbstractTopic<T, byte[], HazelcastInstance> {

    private ITopic<byte[]> topic;

    public HazelcastTopic(String name) {
        super(name, new DefaultSerializer());
    }

    @Override
    public void setClient(HazelcastInstance client) {
        this.client = client;
        topic = client.getTopic(name);
    }

    @Override
    public void subscribe(Subscriber<? super T> s) {
        topic.addMessageListener(message -> {
            byte[] msg = message.getMessageObject();
            Notification<T> notification = serializer.deserialize(msg);
            switch (notification.getKind()) {
                case OnNext:
                    System.out.println(name() + ": Recv\t" + notification.getValue());
                    s.onNext(notification.getValue());
                    break;
                case OnError:
                    s.onError(notification.getThrowable());
                    break;
                case OnCompleted:
                    System.out.println(name() + ": Recv\tComplete");
                    s.onComplete();
                    break;
                default:
            }
        });
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        publish(Notification.createOnNext(t));
    }

    @Override
    public void onError(Throwable t) {
        publish(Notification.createOnError(t));
    }

    @Override
    public void onComplete() {
        publish(Notification.createOnCompleted());
    }

    private void publish(Notification notification) {
        topic.publish(serializer.serialize(notification));
    }

    @Override
    public HazelcastTopic<T> clone() {
        return new HazelcastTopic<>(name);
    }

    public static List<HazelcastTopic> extract(Stream stream, Output output) {
        return AbstractTopic.extractAll(stream, output)
                .stream()
                .filter(topic -> topic instanceof HazelcastTopic)
                .map(topic -> ((HazelcastTopic) topic))
                .collect(Collectors.toList());
    }}
