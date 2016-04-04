import graph_viz.GraphVisualizer;
import org.javatuples.Pair;
import org.junit.Test;
import org.rhea_core.Stream;
import test_data.utilities.Threads;

import javax.swing.*;
import java.util.PriorityQueue;

/**
 * @author Orestis Melkonian
 */
public class Adhoc {
    @Test
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
            super(stream.getGraph());
        }
    }
}
