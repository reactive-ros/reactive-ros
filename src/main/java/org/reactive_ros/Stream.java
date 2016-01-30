package org.reactive_ros;

import org.reactive_ros.internal.io.Listener;
import org.reactive_ros.internal.io.Sink;
import org.reactive_ros.internal.io.Source;
import org.reactive_ros.internal.graph.FlowGraph;
import org.reactive_ros.internal.notifications.Notification;
import org.reactive_ros.internal.output.*;
import org.reactive_ros.internal.expressions.*;
import org.reactive_ros.internal.expressions.backpressure.*;
import org.reactive_ros.internal.expressions.combining.*;
import org.reactive_ros.internal.expressions.conditional_boolean.*;
import org.reactive_ros.internal.expressions.creation.*;
import org.reactive_ros.internal.expressions.feedback.*;
import org.reactive_ros.internal.expressions.error_handling.*;
import org.reactive_ros.internal.expressions.filtering.*;
import org.reactive_ros.internal.expressions.transformational.*;
import org.reactive_ros.internal.expressions.utility.*;
import org.reactive_ros.evaluation.EvaluationStrategy;
import org.reactive_ros.evaluation.Serializer;
import org.reactive_ros.util.functions.*;
import org.jgrapht.Graphs;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The basic class used by an end-user of this API. Represents a stream of data and exposes multiple
 * operators to compose complex dataflows.
 * @author Orestis Melkonian
 */
public class Stream<T> implements Serializable { // TODO create

    /**
     * The internal representation of this {@link Stream} as a {@link FlowGraph}.
     */
    private FlowGraph graph;

    /**
     * The {@link Transformer} to connect subsequent operations.
     */
    private Transformer toConnect;

    /**
     * The {@link EvaluationStrategy} to use.
     */
    private static ThreadLocal<EvaluationStrategy> evaluationStrategy = ThreadLocal.withInitial(() -> null);

    public Stream(FlowGraph graph) {
        this.graph = graph;
        this.toConnect = graph.getConnectNode();
    }

    public Stream(FlowGraph graph, Transformer toConnect) {
        this.graph = graph;
        this.toConnect = toConnect;
    }

    public FlowGraph getGraph() {
        return graph;
    }

    public Transformer getToConnect() {
        return toConnect;
    }

    /**
     * Must always be set prior to usage.
     * @param evaluationStrategy the {@link EvaluationStrategy} to use for evaluating this {@link Stream}
     */
    public static void setEvaluationStrategy(EvaluationStrategy evaluationStrategy) {
        Stream.evaluationStrategy.set(evaluationStrategy);
    }

    /**
     * Duplicates this {@link Stream}.
     * @return the deep-copy of this {@link Stream}
     */
    public Stream<T> copy() {
        FlowGraph copy = graph.copy();
        return new Stream<>(copy, toConnect.clone());
    }

    /**
     * Operators
     */
    /* =======================================================
     * No Input
     * ======================================================= */
    private static <T> Stream<T> init(Transformer<T> expr) {
        FlowGraph ret = new FlowGraph();
        ret.addConnectVertex(expr);
        return new Stream<>(ret, ret.getConnectNode());
    }
    // =======================================================
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#from(java.lang.Iterable)">rx-java.from</a> */
    public static <T> Stream<T> from(Iterable<? extends T> values) {
        return init(new FromExpr<>(values));
    }
    /**
     * Constructs a {@link Stream} from a {@link Source}.
     * @param source the source implementation
     * @return the resulting {@link Stream}
     */
    public static <T> Stream<T> from(Source<T> source) {
        return init(new FromSource<>(source));
    }
    /**
     * Constructs a {@link Stream} from a {@link Listener}.
     * @param listener the listener to be used
     * @return the resulting {@link Stream}
     */
    public static <T> Stream<T> fromListener(Listener<T> listener) {
        return init(new FromListener<T>(listener));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#error(java.lang.Throwable)">rx-java.error</a> */
    public static <T> Stream<T> error(Throwable t) {
        return init(new ErrorExpr<>(t));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#empty()">rx-java.empty</a> */
    public static <T> Stream<T> empty() {
        return init(new EmptyExpr<>());
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#never()">rx-java.never</a> */
    public static <T> Stream<T> never() {
        return init(new NeverExpr<>());
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#defer(rx.functions.Func0)">rx-java.defer</a> */
    public static <T> Stream<T> defer(Func0<Stream<T>> streamFactory) {
        return init(new DeferExpr<>(streamFactory));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#interval(long, java.util.concurrent.TimeUnit)">rx-java.interval</a> */
    public static Stream<Long> interval(long interval, TimeUnit unit) {
        return init(new IntervalExpr(interval, unit));
    }
    /**
     * Entry point. Necessary whenever feedback loop operator is used.
     */
    public static <T> Stream<T> entry() {
        return init(new EntryPointExpr<T>());
    }

    /* =======================================================
     * Single Input
     * ======================================================= */
    private <R> Stream<R> attach(Transformer<R> expr) {
        graph.addVertex(expr);
        graph.addEdge(toConnect, expr);
        graph.setConnectNode(expr);
        return new Stream<>(graph, expr);
    }
    // =======================================================
    /**
     * Feedback Loop
     */
    public Stream<T> loop(Stream<T> stream) {
        FlowGraph graph = stream.getGraph();
        Transformer<T> entry = graph.getEntryPoint();
        Transformer<T> exit = new ExitPointExpr<>();

        if (entry.getClass() != EntryPointExpr.class)
            throw new RuntimeException("Cannot use loop with no EntryPointExpr as the first Transformer of the given Stream");

        // Attach MergeMultiExpr
        MergeMultiExpr<T> merge = new MergeMultiExpr<>();

        // Add Merge and Exit
        this.graph.addVertex(merge);
        this.graph.addVertex(exit);

        // Create feedback edge
        this.graph.addEdge(exit, merge);
        this.graph.attach(merge);

        // Connect to stream's graph
        Graphs.addAllVertices(this.getGraph(), graph.vertexSet());
        Graphs.addAllEdges(this.getGraph(), graph, graph.edgeSet());
        this.graph.setConnectNode(graph.getConnectNode());
        this.graph.addEdge(merge, entry);

        // Attach ExitPoint
        this.graph.attach(exit);

        this.graph.setConnectNode(exit);

//        return this;
        return new Stream<>(this.graph, this.graph.getConnectNode());
    }
    public Stream<T> loop(Func1<Stream<T>, Stream<T>> streamFunc) {
        return loop(streamFunc.call(Stream.<T>entry()));
    }
    public Stream<T> loop(Func1<Stream<T>, Stream<T>> streamFunc, long time, TimeUnit timeUnit) {
        return loop(streamFunc.call(Stream.<T>entry().sample(1, TimeUnit.SECONDS)));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#map(rx.functions.Func1)">rx-java.map</a> */
    public <R> Stream<R> map(Func1<? super T, ? extends R> mapper) {
        return attach(new MapExpr<>(mapper));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#filter(rx.functions.Func1)">rx-java.filter</a> */
    public Stream<T> filter(Func1<? super T, Boolean> predicate) {
        return attach(new FilterExpr<T>(predicate));
//        return concatMap(t -> (filter.call(t)) ? just(t) : empty());
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#skipUntil(rx.Observable)">rx-java.skipUntil</a> */
    public <U> Stream<T> skipUntil(Stream<U> other) {
        return attach(new SkipUntilExpr<>(other));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#skipWhile(rx.functions.Func1)">rx-java.skipWhile</a> */
    public Stream<T> skipWhile(Func1<? super T, Boolean> predicate) {
        return attach(new SkipWhileExpr<>(predicate));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#takeUntil(rx.Observable)">rx-java.takeUntil</a> */
    public <U> Stream<T> takeUntil(Stream<U> other) { // TODO
        return attach(new TakeUntilExpr<>(other));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#takeWhile(rx.functions.Func1)">rx-java.takeWhile</a> */
    public Stream<T> takeWhile(Func1<? super T, Boolean> predicate) {
        return attach(new TakeWhileExpr<>(predicate));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#exists(rx.functions.Func1)">rx-java.exists</a> */
    public Stream<Boolean> exists(Func1<? super T, Boolean> predicate) {
        return attach(new ExistsExpr<>(predicate));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#onErrorResumeNext(rx.Observable)">rx-java.onErrorResumeNext</a> */
    public Stream<T> onErrorResumeNext(Stream<T> resumeStream) { // TODO
        return attach(new OnErrorResumeExpr<>(resumeStream));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#onErrorReturn(rx.functions.Func1)">rx-java.onErrorReturn</a> */
    public Stream<T> onErrorReturn(Func1<Throwable, ? extends T> resumeFunction) {
        return attach(new OnErrorReturnExpr<>(resumeFunction));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#retry(long)">rx-java.retry</a> */
    public Stream<T> retry(int count) {
        return attach(new RetryExpr<>(count));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#take(int)">rx-java.take</a> */
    public Stream<T> take(int count) {
        return attach(new TakeExpr<>(count));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#skip(int)">rx-java.skip</a> */
    public Stream<T> skip(int count) {
        return attach(new SkipExpr<>(count));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#distinct()">rx-java.distinct</a> */
    public Stream<T> distinct() {
        return attach(new DistinctExpr<>());
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#repeat(long)">rx-java.repeat</a> */
    public Stream<T> repeat(int count) {
        return attach(new RepeatExpr<>(count));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#scan(rx.functions.Func2)">rx-java.scan</a> */
    public <R> Stream<R> scan(R seed, Func2<R, ? super T, R> accumulator) {
        return attach(new ScanExpr<>(seed, accumulator));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#scan(R, rx.functions.Func2)">rx-java.scan</a> */
    public Stream<T> scan(Func2<T, T, T> accumulator) {
        return attach(new SimpleScanExpr<>(accumulator));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#buffer(int)">rx-java.buffer</a> */
    public Stream<List<T>> buffer(int count) {
        return attach(new BufferExpr<>(count, -1, null));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#buffer(long, java.util.concurrent.TimeUnit)">rx-java.buffer</a> */
    public Stream<List<T>> buffer(long timespan, TimeUnit unit) {
        return attach(new BufferExpr<>(-1, timespan, unit));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#window(int)">rx-java.window</a> */
    public Stream<Stream<T>> window(int count) {
        Stream<List<T>> buffered = buffer(count);
        return buffered.map(Stream::from);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#window(long, java.util.concurrent.TimeUnit)">rx-java.window</a> */
    public Stream<Stream<T>> window(long timespan, TimeUnit unit) {
        Stream<List<T>> buffered = buffer(timespan, unit); // TODO timing issue
        return buffered.map(Stream::from);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#cache()">rx-java.cache</a> */
    public Stream<T> cache() {
        return attach(new CacheExpr<>());
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#finallyDo(rx.functions.Action0)">rx-java.finallyDo</a> */
    public Stream<T> finallyDo(Action0 action) {
        return attach(new Action0Expr<>(action, "finally"));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#doOnCompleted(rx.functions.Action0)">rx-java.doOnCompleted</a> */
    public Stream<T> doOnCompleted(Action0 action) {
        return attach(new Action0Expr<>(action, "onComplete"));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#doOnTerminate(rx.functions.Action0)">rx-java.doOnTerminate</a> */
    public Stream<T> doOnTerminate(Action0 action) {
        return attach(new Action0Expr<>(action, "onTerminate"));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#doOnSubscribe(rx.functions.Action0)">rx-java.doOnSubscribe</a> */
    public Stream<T> doOnSubscribe(Action0 action) {
        return attach(new Action0Expr<>(action, "onSubscribe"));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#doOnUnsubscribe(rx.functions.Action0)">rx-java.doOnUnsubscribe</a> */
    public Stream<T> doOnUnsuscribe(Action0 action) {
        return attach(new Action0Expr<>(action, "onUnsubscribe"));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#doOnError(rx.functions.Action1)">rx-java.doOnError</a> */
    public Stream<T> doOnError(Action1<Throwable> action) {
        return attach(new Action1Expr<>(action, null, null));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#doOnNext(rx.functions.Action1)">rx-java.doOnNext</a> */
    public Stream<T> doOnNext(Action1<? super T> action) {
        return attach(new Action1Expr<>(null, action, null));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#doOnRequest(rx.functions.Action1)">rx-java.doOnRequest</a> */
    public Stream<T> doOnRequest(Action1<Long> action) {
        return attach(new Action1Expr<>(null, null, action));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#delay(long, java.util.concurrent.TimeUnit)">rx-java.delay</a> */
    public Stream<T> delay(long delay, TimeUnit unit) {
        return attach(new DelayExpr<>(delay, unit));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#sample(long, java.util.concurrent.TimeUnit)">rx-java.sample</a> */
    public Stream<T> sample(long time, TimeUnit timeUnit) {
        return attach(new SampleExpr<>(time, timeUnit));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#timeout(long, java.util.concurrent.TimeUnit)">rx-java.timeout</a> */
    public Stream<T> timeout(long time, TimeUnit timeUnit) {
        return attach(new TimeoutExpr<>(time, timeUnit));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#onBackpressureBuffer(long, rx.functions.Action0)">rx-java.onBackpressureBuffer</a> */
    public Stream<T> onBackpressureBuffer(long capacity, Action0 onOverflow) {
        return attach(new BackpressureBufferExpr<>(capacity, onOverflow));
    }
    /** @see <a href=""http://reactivex.io/RxJava/javadoc/rx/Observable.html#onBackpressureDrop(rx.functions.Action1)>rx-java.onBackpressureDrop</a> */
    public Stream<T> onBackpressureDrop(Action1<? super T> onDrop) {
        return attach(new BackpressureDropExpr<>(onDrop));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#onBackpressureLatest()">rx-java.onBackpressureLatest</a> */
    public Stream<T> onBackpressureLatest() {
        return attach(new BackpressureLatestExpr<>());
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#materialize()">rx-java.materialize</a> */
    public Stream<Notification<T>> materialize() {
        return attach(new MaterializeExpr<T>());
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#dematerialize()">rx-java.dematerialize</a> */
    public <T2> Stream<T2> dematerialize() {
        return attach(new DematerializeExpr<>());
    }
    /**
     * Serializes this {@link Stream} using given {@link Serializer}.
     * @param ser the {@link Serializer} to use
     * @param <B> the byte representation used by the {@link Serializer}
     * @return the serialized {@link Stream}
     */
    public <B> Stream<B> serialize(Serializer<B> ser) {
        return this.materialize()
                   .map(ser::serialize);
    }
    /**
     * Deserializes this {@link Stream} using given {@link Serializer}.
     * @param ser the {@link Serializer} to use
     * @param <B> the byte representation used by the {@link Serializer}
     * @return the deserialized {@link Stream}
     */
    public <B, T2> Stream<T2> deserialize(Serializer<B> ser) {
        return this.map(m -> ser.deserialize(((B) m)))
                   .dematerialize();
    }


    /* =======================================================
     * Multiple Input
     * ======================================================= */
    private static <R> Stream<R> attachMulti(Transformer<R> expr, Stream... streams) {
        List<FlowGraph> inputs = Arrays.stream(streams).map(Stream::getGraph).collect(Collectors.toList());
        FlowGraph ret = new FlowGraph(Arrays.asList(streams), inputs);
        ret.attachMulti(expr);
        return new Stream<>(ret, expr);
    }
    // =======================================================
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#amb(java.lang.Iterable)">rx-java.amb</a> */
    public static <T> Stream<T> amb(List<Stream<T>> sources) {
        return attachMulti(new AmbExpr<>(), sources.toArray(new Stream[sources.size()]));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#merge(rx.Observable)">rx-java.merge</a> */
    public static <T> Stream<T> merge(Stream<? extends Stream<? extends T>> streams) {
        return streams.attach(new MergeExpr<>());
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#concat(rx.Observable)">rx-java.concat</a> */
    public static <T> Stream<T> concat(Stream<? extends Stream<? extends T>> streams) {
        return streams.attach(new ConcatExpr<T>());
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#concat(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable)">rx-java.concat</a> */
    public static <T> Stream<T> concat(List<? extends Stream<? extends T>> streams) {
        return attachMulti(new ConcatMultiExpr<T>(), streams.toArray(new Stream[streams.size()]));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#zip(rx.Observable, rx.Observable, rx.functions.Func2)">rx-java.zip</a> */
    public static <T1,T2,R> Stream<R> zip(Stream<? extends T1> p1, Stream<? extends T2> p2, Func2<? super T1, ? super T2, R> combiner) {
        return attachMulti(new ZipExpr<>("zip", combiner), p1, p2);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#zip(rx.Observable, rx.Observable, rx.Observable, rx.functions.Func3)">rx-java.zip</a> */
    public static <T1,T2,T3,R> Stream<R> zip(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Func3<? super T1, ? super T2, ? super T3, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("zip", combiner), p1, p2, p3);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#zip(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func4)">rx-java.zip</a> */
    public static <T1,T2,T3,T4,R> Stream<R> zip(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("zip", combiner), p1, p2, p3, p4);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#zip(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func5)">rx-java.zip</a> */
    public static <T1,T2,T3,T4,T5,R> Stream<R> zip(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("zip", combiner), p1, p2, p3, p4, p5);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#zip(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func6)">rx-java.zip</a> */
    public static <T1,T2,T3,T4,T5,T6,R> Stream<R> zip(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Stream<? extends T6> p6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("zip", combiner), p1, p2, p3, p4, p5, p6);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#zip(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func7)">rx-java.zip</a> */
    public static <T1,T2,T3,T4,T5,T6,T7,R> Stream<R> zip(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Stream<? extends T6> p6, Stream<? extends T7> p7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("zip", combiner), p1, p2, p3, p4, p5, p6, p7);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#zip(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func8)">rx-java.zip</a> */
    public static <T1,T2,T3,T4,T5,T6,T7,T8,R> Stream<R> zip(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Stream<? extends T6> p6, Stream<? extends T7> p7, Stream<? extends T8> p8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("zip", combiner), p1, p2, p3, p4, p5, p6, p7, p8);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#zip(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func9)">rx-java.zip</a> */
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,R> Stream<R> zip(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Stream<? extends T6> p6, Stream<? extends T7> p7, Stream<? extends T8> p8, Stream<? extends T9> p9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("zip", combiner), p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#combineLatest(rx.Observable, rx.Observable, rx.functions.Func2)">rx-java.combineLatest</a> */
    public static <T1,T2,R> Stream<R> combineLatest(Stream<? extends T1> p1, Stream<? extends T2> p2, Func2<? super T1, ? super T2, R> combiner) {
        return attachMulti(new ZipExpr<>("combine", combiner), p1, p2);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#combineLatest(rx.Observable, rx.Observable, rx.Observable, rx.functions.Func3)">rx-java.combineLatest</a> */
    public static <T1,T2,T3,R> Stream<R> combineLatest(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Func3<? super T1, ? super T2, ? super T3, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("combine", combiner), p1, p2, p3);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#combineLatest(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func4)">rx-java.combineLatest</a> */
    public static <T1,T2,T3,T4,R> Stream<R> combineLatest(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("combine", combiner), p1, p2, p3, p4);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#combineLatest(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func5)">rx-java.combineLatest</a> */
    public static <T1,T2,T3,T4,T5,R> Stream<R> combineLatest(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("combine", combiner), p1, p2, p3, p4, p5);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#combineLatest(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func6)">rx-java.combineLatest</a> */
    public static <T1,T2,T3,T4,T5,T6,R> Stream<R> combineLatest(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Stream<? extends T6> p6, Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("combine", combiner), p1, p2, p3, p4, p5, p6);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#combineLatest(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func7)">rx-java.combineLatest</a> */
    public static <T1,T2,T3,T4,T5,T6,T7,R> Stream<R> combineLatest(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Stream<? extends T6> p6, Stream<? extends T7> p7, Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("combine", combiner), p1, p2, p3, p4, p5, p6, p7);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#combineLatest(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func8)">rx-java.combineLatest</a> */
    public static <T1,T2,T3,T4,T5,T6,T7,T8,R> Stream<R> combineLatest(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Stream<? extends T6> p6, Stream<? extends T7> p7, Stream<? extends T8> p8, Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("combine", combiner), p1, p2, p3, p4, p5, p6, p7, p8);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#combineLatest(rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.Observable, rx.functions.Func9)">rx-java.combineLatest</a> */
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,R> Stream<R> combineLatest(Stream<? extends T1> p1, Stream<? extends T2> p2, Stream<? extends T3> p3, Stream<? extends T4> p4, Stream<? extends T5> p5, Stream<? extends T6> p6, Stream<? extends T7> p7, Stream<? extends T8> p8, Stream<? extends T9> p9, Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combiner) {
        return attachMulti(new ZipExpr<>("combine", combiner), p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    /* =======================================================
     * Macros
     * ======================================================= */
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#just(T, T, T, T, T, T, T, T, T, T)">rx-java.just</a> */
    @SafeVarargs
    public static <T> Stream<T> just(T... values) {
        return from(Arrays.asList(values));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#from(T[])">rx-java.from</a> */
    @SafeVarargs
    public static <T> Stream<T> from(T... values) {
        return from(Arrays.asList(values));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#merge(java.lang.Iterable)">rx-java.merge</a> */
    public static <T> Stream<T> merge(List<? extends Stream<? extends T>> streams) {
//        return merge(from(streams));
        return attachMulti(new MergeMultiExpr<T>(), streams.toArray(new Stream[streams.size()]));
    }
    @SafeVarargs
    public static <T> Stream<T> merge(Stream<T>... streams) {
        return merge(Arrays.asList(streams));
    }
    @SafeVarargs
    public static <T> Stream<T> concat(Stream<? extends T>... streams) {
//        return concat(from(streams));
        return concat(Arrays.asList(streams));
    }
    public Stream<T> concatWith(Stream<T> other) {
        return Stream.concat(this, other);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#amb(java.lang.Iterable)">rx-java.amb</a> */
    @SafeVarargs
    public static <T> Stream<T> amb(Stream<T>... sources) {
        return amb(Arrays.asList(sources));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#all(rx.functions.Func1)">rx-java.all</a> */
    public Stream<Boolean> all(Func1<? super T, Boolean> predicate) {
        return exists(b -> !predicate.call(b)).map(b -> !b);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#contains(java.lang.Object)">rx-java.contains</a> */
    public Stream<Boolean> contains(T obj) {
        return exists(t -> (obj == null) ? t == null : t.equals(obj));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#takeLast(int)">rx-java.takeLast</a> */
    public Stream<T> takeLast(int count) {
        return take(-count);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#skipLast(int)">rx-java.skipLast</a> */
    public Stream<T> skipLast(int count) {
        return skip(-count);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#first()">rx-java.ignoreElements</a> */
    public Stream<T> first() {
        return take(1);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#last()">rx-java.ignoreElements</a> */
    public Stream<T> last() {
        return takeLast(1);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#elementAt(int)">rx-java.elementAt</a> */
    public Stream<T> elementAt(int index) {
        return take(index + 1).last();
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#retry()">rx-java.retry</a> */
    public Stream<T> retry() {
        return retry(-1);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#ofType(java.lang.Class)">rx-java.ofType</a> */
    public <R> Stream<R> ofType(Class<R> klass) {
        return filter(klass::isInstance).cast(klass);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#ignoreElements()">rx-java.ignoreElements</a> */
    public Stream<T> ignoreElements() {
        return filter(t -> false);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#reduce(rx.functions.Func2)">rx-java.reduce</a> */
    public Stream<T> reduce(Func2<T, T, T> accumulator) {
        return scan(accumulator).takeLast(1);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#reduce(R, rx.functions.Func2)">rx-java.reduce</a> */
    public <R> Stream<R> reduce(R initialSeed, Func2<R, ? super T, R> accumulator) {
        return scan(initialSeed, accumulator).last();
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#count()">rx-java.count</a> */
    public Stream<Integer> count() {
        return reduce(0, (t1, t2) -> t1 + 1);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#collect(rx.functions.Func0, rx.functions.Action2)">rx-java.collect</a> */
    public <R> Stream<R> collect(Func0<R> stateFactory, Action2<R, ? super T> collector) {
        return reduce(stateFactory.call(), (state, value) -> {
            collector.call(state, value);
            return state;
        });
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#toList()">rx-java.toList</a> */
    public Stream<List<T>> toList() {
        return collect(ArrayList::new, List::add);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#toSortedList(rx.functions.Func2)">rx-java.toSortedList</a> */
    public Stream<List<T>> toSortedList(Func2<? super T, ? super T, Integer> comparator) {
        return toList().map(l -> {
            Collections.sort(l, comparator::call);
            return l;
        });
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#toMap(rx.functions.Func1)">rx-java.toMap</a> */
    public <K> Stream<Map<K,T>> toMap(Func1<? super T, ? extends K> keySelector) {
        return collect(HashMap::new, (map, item) -> map.put(keySelector.call(item), item));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#toMap(rx.functions.Func1, rx.functions.Func1)">rx-java.toMap</a> */
    public <K, V> Stream<Map<K,V>> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        return collect(HashMap::new, (map, item) -> map.put(keySelector.call(item), valueSelector.call(item)));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#toMap(rx.functions.Func1, rx.functions.Func1, rx.functions.Func0)">rx-java.toMap</a> */
    public <K, V> Stream<Map<K,V>> toMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<Map<K, V>> mapFactory) {
        return collect(mapFactory, (map, item) -> map.put(keySelector.call(item), valueSelector.call(item)));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#repeat()">rx-java.repeat</a> */
    public Stream<T> repeat() {
        return repeat(-1);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#range(int, int)">rx-java.range</a> */
    public static Stream<Integer> range(int start, int count) {
        return from(IntStream.range(start, start + count).boxed().toArray(Integer[]::new));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#timer(long, long, java.util.concurrent.TimeUnit)">rx-java.timer</a> */
    public static Stream<Long> timer(long delay, long period, TimeUnit unit) {
        return interval(period, unit).delay(delay, unit);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#onBackpressureBuffer()">rx-java.onBackpressureBuffer</a> */
    public Stream<T> onBackpressureBuffer() {
        return onBackpressureBuffer(-1, null);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#onBackpressureBuffer(long)">rx-java.onBackpressureBuffer</a> */
    public Stream<T> onBackpressureBuffer(long capacity) {
        return onBackpressureBuffer(capacity, null);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#onBackpressureDrop()">rx-java.onBackpressureDrop</a> */
    public Stream<T> onBackpressureDrop() {
        return onBackpressureDrop(null);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#flatMap(rx.functions.Func1)">rx-java.flatMap</a> */
    public <R> Stream<R> flatMap(Func1<? super T, ? extends Stream<? extends R>> func) {
        return merge(map(func));
    }
    public <R> Stream<R> concatMap(Func1<? super T,? extends Stream<? extends R>> func) {
        return concat(map(func));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#startWith(rx.Observable)">rx-java.startWith</a> */
    public Stream<T> startWith(Stream<T> values) {
        return concat(values, this);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#startWith(java.lang.Iterable)">rx-java.startWith</a> */
    public Stream<T> startWith(List<T> values) {
        return startWith(from(values));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#startWith(T, T, T, T, T, T, T, T, T)">rx-java.startWith</a> */
    @SafeVarargs
    public final Stream<T> startWith(T... values) {
        return startWith(Arrays.asList(values));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#sequenceEqual(rx.Observable, rx.Observable, rx.functions.Func2)">rx-java.sequenceEqual</a> */
    public static <T> Stream<Boolean> sequenceEqual(Stream<? extends T> first, Stream<? extends T> second, Func2<? super T, ? super T, Boolean> equality) {
        return zip(first, second, equality).all(b -> b);
    }
    public static <T> Stream<Boolean> sequenceEqual(Stream<? extends T> first, Stream<? extends T> second) {
        return sequenceEqual(first, second, Object::equals);
    }
    public Stream<Boolean> equal(Stream<? extends T> other) {
        return sequenceEqual(this, other);
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#cast(java.lang.Class)">rx-java.cast</a> */
    public <R> Stream<R> cast(Class<R> klass) {
        return map(klass::cast);
    }
    public Stream<T> id() {
        return this.map(s -> s);
    }
    public static <T, Resource> Stream<T> using(Func0<Resource> resourceFactory,
                                                Func1<? super Resource,? extends Stream<? extends T>> streamFactory,
                                                Action1<? super Resource> disposeAction) {
        return init(new UsingExpr<>(resourceFactory, streamFactory, disposeAction));
    }

    public static Stream<Integer> nat() {
        return Stream.just(1)
                .loop(s -> s.map(i -> i + 1))
                .startWith(1);
    }

    /**
     *  Evaluation
     */
    private void subscribe(Output output) {
        // TODO Optimize

        // Evaluate
        if (evaluationStrategy.get() == null)
            throw new RuntimeException("EvaluationStrategy not set.");
        evaluationStrategy.get().evaluate(this, output);
    }

    // Expose convenient method calls
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#subscribe(rx.functions.Action1)">rx-java.subscribe</a> */
    public void subscribe(Action1<? super T> onNext) {
        subscribe(new ActionOutput<>(onNext));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#subscribe(rx.functions.Action1, rx.functions.Action1)">rx-java.subscribe</a> */
    public void subscribe(Action1<? super T> onNext, Action1<Throwable> onError) {
        subscribe(new ActionOutput<>(onNext, onError));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#subscribe(rx.functions.Action1, rx.functions.Action1, rx.functions.Action0)">rx-java.subscribe</a> */
    public void subscribe(Action1<? super T> onNext, Action1<Throwable> onError, Action0 onComplete) {
        subscribe(new ActionOutput<>(onNext, onError, onComplete));
    }
    /** @see <a href="http://reactivex.io/RxJava/javadoc/rx/Observable.html#subscribe()">rx-java.subscribe</a> */
    public void subscribe() {
        subscribe(new NoopOutput());
    }

    /**
     * Subscribes to the given {@link Sink}.
     * @param sink the sink to connect to
     */
    public void subscribe(Sink<T> sink) {
        subscribe(new SinkOutput<>(sink));
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Stream && graph.equals(((Stream) obj).getGraph());
    }
    @Override
    public String toString() {
        return "Stream: " + graph.toSimpleString();
    }
    @Override
    public Stream<T> clone() {
        return this.copy();
    }

    public BlockingStream<T> toBlocking() {
        return BlockingStream.from(this);
    }

    public void printErr() {
        this.subscribe((Action1<T>) System.err::println);
    }
    public void print() {
        this.subscribe((Action1<T>) System.out::println);
    }
}
