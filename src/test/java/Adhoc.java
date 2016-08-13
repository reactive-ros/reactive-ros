import org.javatuples.Pair;
import org.junit.Test;
import org.rhea_core.Stream;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.combining.ZipExpr;
import org.rhea_core.internal.graph.FlowGraph;
import org.rhea_core.optimization.OptimizationStrategy;
import org.rhea_core.optimization.optimizers.NodeMerger;

import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import graph_viz.GraphVisualizer;
import test_data.utilities.Threads;

/**
 * @author Orestis Melkonian
 */
public class Adhoc {

//    @Test
    public void timeLoop() {
        Stream<Integer> s = Stream.just(0).timedLoop(entry -> entry.map(i -> i + 1), 1, TimeUnit.DAYS);
        GraphVisualizer.display(s);

        Threads.sleep();
    }

    @Test
    public void zip() {
        // TODO add use-case
        Stream s = Stream.zip(
            Stream.range(0, 1).map(i -> i + 1),
            Stream.range(10, 1).map(i -> i + 1),
            (i1, i2) -> i1 + i2
        );
        FlowGraph g = s.getGraph();
        new NodeMerger(1).optimize(g);

        for (Transformer t : g.vertices())
            if (t instanceof ZipExpr) {
                ZipExpr z = (ZipExpr) t;
                System.out.println(((ZipExpr) t).combiner2.call(1, 2));
            }
        GraphVisualizer.displayAt(s.getGraph(), 100, 0);

        Threads.sleep();
    }

//    @Test
    public void loop() {
        Stream<Integer> s = Stream.just(0).loopN(entry -> entry.map(i -> i + 1), 10);
        GraphVisualizer.display(s);

        Threads.sleep();
    }

//    @Test
    public void split() {

        Stream<Integer> s0 = Stream.just(0, 1);

        Stream<Integer> s1 = s0.map(i -> i + 1);
        Stream<Integer> s2 = s0.map(i -> i + 1);

        Stream<Integer> s3 = Stream.zip(s1, s2, (x, y) -> x);

        System.out.println(s0.getToConnect());
        System.out.println(s1.getToConnect());
        System.out.println(s2.getToConnect());
        System.out.println(s3.getToConnect());

        GraphVisualizer.display(s3);

        /*Stream<Integer> a1 = s1.map(i -> i + 1);
        Stream<Integer> a2 = s1.map(i -> i + 2);

        Stream<Integer> s = Stream.zip(a1, a2, (x, y) -> x + y);

        GraphVisualizer.display(s);*/

        Threads.sleep();
    }

//    @Test
    public void test() throws InterruptedException {

        Stream<?> stream = Stream.using(
                () -> new Pair<>(new PriorityQueue<Integer>(), new PriorityQueue<Integer>()),
                queues -> Stream.just(1).loop(e -> {
                    IntStream s = new IntStream(e);
                    return IntStream.mergeSort(
                            IntStream.mergeSort(s.multiply(2), s.multiply(3), queues.getValue0()),
                            s.multiply(5),
                            queues.getValue1()
                    );
                })
                .startWith(1)
                .distinct()
                .take(20),
                queues -> System.out.print(""));

        GraphVisualizer.display(stream);
        Threads.sleep();
    }

    private static class IntStream extends Stream<Integer> {

        public IntStream multiply(int constant) {
            return new IntStream(this.map(i -> i * constant));
        }

        public static Stream<Integer> mergeSort(Stream<Integer> fst, Stream<Integer> snd, PriorityQueue<Integer> queue) {
            return Stream.<Integer, Integer, Integer>zip(
                    fst, snd,
                    (x, y) -> {
                        int min = Math.min(x, y);
                        queue.add(Math.max(x, y));
                        if (min < queue.peek())
                            return min;
                        queue.add(min);
                        return queue.poll();
                    }
            ).concatWith(Stream.from(queue));
        }

        public IntStream(Stream<Integer> stream) {
            super(stream.getGraph(), null);
        }
    }
}
