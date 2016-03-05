package org.rhea_core.evaluation;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import org.rhea_core.internal.notifications.Notification;
import java.nio.charset.Charset;

/**
 * Default serializer using JsonIO.
 * @author Orestis Melkonian
 */
public class DefaultSerializer implements Serializer<byte[]> {
    private static final String complete = "C";
    private static final String error = "E";
    private static final String next = "N";

    @Override
    public byte[] serialize(Notification notification) {
        Notification.Kind kind = notification.getKind();
        if (kind == Notification.Kind.OnCompleted) // onCompleted
            return complete.getBytes();
        else if (kind == Notification.Kind.OnError) // onError
            return (error + JsonWriter.objectToJson(notification.getThrowable())).getBytes();
        else // onNext
            return (next + JsonWriter.objectToJson(notification.getValue())).getBytes();
    }

    @Override
    public Notification deserialize(byte[] buffer) {
        String msg = new String(buffer, Charset.defaultCharset());
        String label = msg.substring(0, 1);
        String content = msg.substring(1);

        switch (label) {
            case complete: // onCompleted
                return Notification.createOnCompleted();
            case error: // onError
                return Notification.createOnError((Throwable) JsonReader.jsonToJava(content));
            default: // onNext
                return Notification.createOnNext(JsonReader.jsonToJava(content));
        }
    }
}
