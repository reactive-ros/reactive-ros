import org.reactive_ros.internal.output.Output;

import java.io.Serializable;

/**
 * @author Orestis Melkonian
 */
public class Example implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;
    private Outputt output;

    public Example(Outputt output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return output.toString();
    }
}
