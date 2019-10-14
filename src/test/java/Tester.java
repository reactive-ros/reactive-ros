import graph_viz.GraphVisualizer;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.rhea_core.Stream;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.combining.MergeExpr;
import org.rhea_core.internal.expressions.combining.MergeMultiExpr;
import org.rhea_core.internal.expressions.combining.ZipExpr;
import org.rhea_core.internal.expressions.creation.FromExpr;
import org.rhea_core.internal.expressions.transformational.MapExpr;
import org.rhea_core.internal.graph.FlowGraph;
import org.rhea_core.optimization.DefaultOptimizationStrategy;
import org.rhea_core.optimization.OptimizationStrategy;
import org.rhea_core.optimization.optimizers.NodeMerger;
import org.rhea_core.optimization.optimizers.Optimizer;
import org.rhea_core.optimization.optimizers.ProactiveFiltering;
import org.rhea_core.util.IdMinter;

import test_data.Collections;
import test_data.utilities.Colors;
import test_data.utilities.Threads;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Orestis Melkonian
 */
public class Tester {

    @Test
    public void split() {
        Stream<Integer> s0 = Stream.just(0, 1, 2);

        Stream<Integer> s1 = s0.map(i -> i + 1);
        Stream<Integer> s2 = s0.map(i -> i + 2);
        Stream<Integer> s3 = s0.map(i -> i + 3);

        Stream<Integer> s = Stream.merge(s1, s2, s3);


        FlowGraph g = s.getGraph();
        List<Transformer> roots = g.getRoots();
        assert roots.size() == 1;
        Transformer root = roots.get(0);
        assert root instanceof FromExpr;

        List<Transformer> successors = g.successors(root);
        assert successors.size() == 3;

        Transformer map1 = successors.get(0);
        assert map1 instanceof MapExpr;
        Transformer map2 = successors.get(1);
        assert map2 instanceof MapExpr;
        Transformer map3 = successors.get(2);
        assert map3 instanceof MapExpr;

        assert g.successors(map1).size() == 1;
        assert g.successors(map2).size() == 1;
        assert g.successors(map3).size() == 1;

        Transformer m1 = g.successor(map1);
        assert m1 instanceof MergeMultiExpr;
        Transformer m2 = g.successor(map2);
        assert m2 instanceof MergeMultiExpr;
        Transformer m3 = g.successor(map3);
        assert m3 instanceof MergeMultiExpr;

        assert m1 == m2;
        assert m2 == m3;

        assert m1 == g.getConnectNode();

        // GraphVisualizer.display(g);
        // Threads.sleep();
    }

   // @Test
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
    public void minter() {
        new Thread(() -> System.out.println(IdMinter.next())).run();
        new Thread(() -> System.out.println(IdMinter.next())).run();
        new Thread(() -> System.out.println(IdMinter.next())).run();
        new Thread(() -> System.out.println(IdMinter.next())).run();
        new Thread(() -> System.out.println(IdMinter.next())).run();
        new Thread(() -> System.out.println(IdMinter.next())).run();
        new Thread(() -> System.out.println(IdMinter.next())).run();
        new Thread(() -> System.out.println(IdMinter.next())).run();
        new Thread(() -> System.out.println(IdMinter.next())).run();
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
