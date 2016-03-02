import graph_viz.GraphVisualizer;
import org.junit.Test;
import org.rhea_core.Stream;
import test_data.utilities.Threads;

/**
 * @author Orestis Melkonian
 */
public class Adhoc {
    @Test
    public void test() throws InterruptedException {

        Stream s = Stream.just(0).loop(i -> i.map(n -> n + 1));
        GraphVisualizer.display(s);

        /*Distributor remote = new Distributor();
        Queue<StreamTask> tasks = new LinkedList<>();

        Func0<EvaluationStrategy> gen = RxjavaEvaluationStrategy::new;
        Output output = new ActionOutput<>(n -> System.out.println(n));
        Stream stream = Stream.just(155);
        StreamTask task = new StreamTask(gen, stream, output);
        tasks.add(task);

        remote.submit(tasks);*/

        Threads.sleep();
    }
}
