import org.junit.Test;
import org.rhea_core.distribution.HazelcastMain;
import test_data.Collections;
import test_data.utilities.Colors;
import test_data.utilities.Threads;

import java.io.*;

/**
 * @author Orestis Melkonian
 */
public class Tester {

    @Test
    public void serialization() {

        // Streams & Outputs
        java.util.stream.Stream.concat(
                Collections.streams().stream(),
                Collections.outputs().stream()
        ).forEach(o -> Colors.print(isSerializable(o) ? Colors.GREEN : Colors.RED, o.toString()));

        Threads.sleep();
    }

    static boolean isSerializable(Object obj) {
        if (!(obj instanceof Serializable))
            return false;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(obj);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            Object obj2 = in.readObject();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Test
    public void setup() throws InterruptedException {
        HazelcastMain.init(null);
        Threads.sleep();
    }
}
