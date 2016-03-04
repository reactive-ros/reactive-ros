import org.junit.Test;
import org.reflections.Reflections;
import org.rhea_core.distribution.Distributor;
import org.rhea_core.evaluation.EvaluationStrategy;
import test_data.utilities.Threads;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Orestis Melkonian
 */
public class Adhoc {
    @Test
    public void test() throws InterruptedException {
        new Distributor();
    }
}
