import org.junit.Test;
import org.reactive_ros.Stream;
import org.reactive_ros.distribution.StreamTask;
import org.reactive_ros.evaluation.EvaluationStrategy;
import org.reactive_ros.internal.output.ActionOutput;
import org.reactive_ros.internal.output.Output;
import org.reactive_ros.util.functions.Func0;
import rx_eval.RxjavaEvaluationStrategy;

import java.io.*;

public class Adhoc {
	@Test
	public void adhoc() throws IOException, ClassNotFoundException {
        Func0<EvaluationStrategy> gen = RxjavaEvaluationStrategy::new;
        Output output = new ActionOutput<>(
                i -> System.out.println(i),
                e -> System.out.println(e),
                () -> System.out.println("Complete")
        );
        Stream stream = Stream.just(1, 2, 3);
        StreamTask task = new StreamTask(gen, stream, output);
        test(task);
	}

    private void print() {
        System.out.println("Complete");
    }


    private static void test(Object obj) throws IOException, ClassNotFoundException {
        System.out.println(deserialize(serialize(obj)));
    }

    private static ByteArrayOutputStream serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        return bos;
    }

    private static Object deserialize(ByteArrayOutputStream bos) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return in.readObject();
    }
}