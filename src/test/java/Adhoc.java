import graph_viz.GraphVisualizer;
import org.junit.Test;
import org.rhea_core.Stream;
import org.rhea_core.distribution.Distributor;
import test_data.utilities.Threads;

/**
 * @author Orestis Melkonian
 */
public class Adhoc {
    @Test
    public void test() throws InterruptedException {
        GraphVisualizer.display(Stream.nat());
        Threads.sleep();
    }
}
