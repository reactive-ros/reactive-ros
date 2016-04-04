import graph_viz.GraphVisualizer;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.rhea_core.Stream;
import org.rhea_core.optimization.DefaultOptimizationStrategy;
import org.rhea_core.optimization.OptimizationStrategy;
import org.rhea_core.optimization.optimizers.NodeMerger;
import org.rhea_core.optimization.optimizers.Optimizer;
import org.rhea_core.optimization.optimizers.ProactiveFiltering;
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
        java.util.stream.Stream.concat(
                Collections.streams().stream(),
                Collections.outputs().stream()
        ).forEach(o -> Colors.print(isSerializable(o) ? Colors.GREEN : Colors.RED, o.toString()));
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

//    @Test
    public void filtering() {
        Stream<?> s = Stream.concat(
                Stream.just(0).map(i -> i+1).take(1),
                Stream.just(666)
        ).filter(i -> i<667);

        GraphVisualizer.displayAt(s, 0, 0);
        new ProactiveFiltering().optimize(s.getGraph());
        GraphVisualizer.displayAt(s, 500, 0);

        Threads.sleep();
    }

//    @Test
    public void node_merge() {
        Stream<?> s = Stream.concat(
                Stream.just(0).id().id().filter(i -> i > 0).filter(i -> i > 0).exists(i -> i > 0),
                Stream.zip(
                        Stream.just(0).id().exists(i -> i > 0).id(),
                        Stream.just(0).id(),
                        (x, y) -> x.hashCode() + y
                ).id()
            );

        GraphVisualizer.displayAt(s, 0, 0);
        new NodeMerger(1).optimize(s.getGraph());
        GraphVisualizer.displayAt(s, 500, 0);

        Threads.sleep();
    }
}
