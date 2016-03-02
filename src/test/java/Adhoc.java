import graph_viz.GraphVisualizer;
import org.junit.Test;
import org.rhea_core.Stream;
import org.rhea_core.distribution.RemoteExecution;
import org.rhea_core.distribution.StreamTask;
import org.rhea_core.evaluation.EvaluationStrategy;
import org.rhea_core.internal.output.ActionOutput;
import org.rhea_core.internal.output.Output;
import org.rhea_core.util.functions.Func0;
import rx_eval.RxjavaEvaluationStrategy;
import test_data.utilities.Threads;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Orestis Melkonian
 */
public class Adhoc {
    @Test
    public void test() throws InterruptedException {

        Stream s = Stream.just(0).loop(i -> i.map(n -> n + 1));
        GraphVisualizer.display(s);

        /*RemoteExecution remote = new RemoteExecution();
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
