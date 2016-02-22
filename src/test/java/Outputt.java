import org.reactive_ros.internal.output.Output;
import org.reactive_ros.util.functions.Action0;
import org.reactive_ros.util.functions.Action1;

/**
 * @author Orestis Melkonian
 */
public class Outputt<T> implements Output {

    private Action1<? super T> lambda;
    private Action1<Throwable> lambda2;
    private Action0 lambda3;

    public Outputt(Action1<? super T> lambda) {
        this.lambda = lambda;
    }

    public Outputt(Action1<? super T> lambda, Action1<Throwable> lambda2) {
        this.lambda = lambda;
        this.lambda2 = lambda2;
    }

    public Outputt(Action1<? super T> lambda, Action1<Throwable> lambda2, Action0 lambda3) {
        this.lambda = lambda;
        this.lambda2 = lambda2;
        this.lambda3 = lambda3;
    }

    public Action1<? super T> getAction() {
        return lambda;
    }

    public Action1<Throwable> getErrorAction() {
        return lambda2;
    }

    public Action0 getCompleteAction() {
        return lambda3;
    }

    @Override
    public String toString() {
        return "150";
    }
}
