import graph_viz.GraphVisualizer;
import org.junit.Test;
import org.rhea_core.Stream;
import org.rhea_core.optimization.EntryRemoval;
import test_data.utilities.Threads;

/**
 * @author Orestis Melkonian
 */
public class Adhoc {
    @Test
    public void test() throws InterruptedException {

        Stream s = Stream.just(0).loop(i -> i.map(n -> n + 1));

        new EntryRemoval().optimize(s.getGraph());

        GraphVisualizer.display(s);

        Threads.sleep();
    }
}
