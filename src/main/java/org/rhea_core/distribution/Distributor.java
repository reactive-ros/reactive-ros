package org.rhea_core.distribution;

import com.hazelcast.core.*;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.rhea_core.Stream;
import org.rhea_core.distribution.annotations.MachineInfo;
import org.rhea_core.distribution.annotations.StrategyInfo;
import org.rhea_core.distribution.graph.DistributedGraph;
import org.rhea_core.distribution.graph.TopicEdge;
import org.rhea_core.distribution.hazelcast.HazelcastTopic;
import org.rhea_core.evaluation.EvaluationStrategy;
import org.rhea_core.internal.expressions.MultipleInputExpr;
import org.rhea_core.internal.expressions.NoInputExpr;
import org.rhea_core.internal.expressions.SingleInputExpr;
import org.rhea_core.internal.expressions.Transformer;
import org.rhea_core.internal.expressions.creation.FromSource;
import org.rhea_core.internal.graph.FlowGraph;
import org.rhea_core.internal.output.MultipleOutput;
import org.rhea_core.internal.output.Output;
import org.rhea_core.internal.output.SinkOutput;
import org.rhea_core.util.ReflectionUtils;
import org.rhea_core.util.functions.Func0;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Distributor {
    HazelcastInstance hazelcast;
    List<MachineInfo> machines;
    List<StrategyInfo> strategies;
    int desiredGranularity;

    Func0<EvaluationStrategy> strategy;

    public Distributor(HazelcastInstance hazelcast) {
        machines = ReflectionUtils.getMachines();
        strategies = ReflectionUtils.getStrategies();
        desiredGranularity = machines.stream().map(MachineInfo::cores).reduce((i1, i2) -> i1 + i2).get();
        this.hazelcast = hazelcast;
    }

    public Distributor() {
        this(Hazelcast.newHazelcastInstance());
    }

    public Distributor(Func0<EvaluationStrategy> strategy) {
        this(Hazelcast.newHazelcastInstance());
        this.strategy = strategy;
    }

    public int getDesiredGranularity() {
        return desiredGranularity;
    }

    public void evaluate(Stream stream, Output output) {

        // TODO decrease granularity { iff (stream.getGraph().size() > desiredGranularity) }

        DistributedGraph graph = new DistributedGraph(stream.getGraph(), this::newTopic);

        Queue<StreamTask> tasks = new LinkedList<>();

        // Run output node first
        HazelcastTopic result = newTopic();
        tasks.add(new StreamTask(strategy, Stream.from(result), output, new ArrayList<>()));

        // Then run each graph vertex as an individual node (reverse BFS)
        Set<Transformer> checked = new HashSet<>();
        Stack<Transformer> stack = new Stack<>();
        for (Transformer root : graph.getRoots())
            new BreadthFirstIterator<>(graph, root).forEachRemaining(stack::push);
        while (!stack.empty()) {
            Transformer toExecute = stack.pop();
            if (checked.contains(toExecute)) continue;

            Set<TopicEdge> inputs = graph.incomingEdgesOf(toExecute);

            FlowGraph innerGraph = new FlowGraph();
            if (toExecute instanceof NoInputExpr) {
                assert inputs.size() == 0;
                // 0 input
                innerGraph.addConnectVertex(toExecute);
            } else if (toExecute instanceof SingleInputExpr) {
                assert inputs.size() == 1;
                // 1 input
                HazelcastTopic input = inputs.iterator().next().getTopic();
                Transformer toAdd = new FromSource(input.clone());
                innerGraph.addConnectVertex(toAdd);
                innerGraph.attach(toExecute);
            } else if (toExecute instanceof MultipleInputExpr) {
                assert inputs.size() > 1;
                // N inputs
                innerGraph.setConnectNodes(inputs.stream()
                        .map(edge -> new FromSource(edge.getTopic().clone()))
                        .collect(Collectors.toList()));
                innerGraph.attachMulti(toExecute);
            }

            // Set outputs according to graph connections
            Set<TopicEdge> outputs = graph.outgoingEdgesOf(toExecute);
            List<Output> list = new ArrayList<>();
            if (toExecute.equals(graph.toConnect))
                list.add(new SinkOutput(result.clone()));
            list.addAll(outputs.stream()
                    .map(TopicEdge::getTopic)
                    .map((Function<HazelcastTopic, SinkOutput<Object>>) sink -> new SinkOutput(sink))
                    .collect(Collectors.toList()));
            Output outputToExecute = (list.size() == 1) ? list.get(0) : new MultipleOutput(list);

            // Schedule for execution
            tasks.add(new StreamTask(strategy, new Stream(innerGraph), outputToExecute, new ArrayList<>()));

            checked.add(toExecute);
        }

        // Execute
        submit(tasks);
    }

    private void executeOn(Runnable task, String ip) {
        for (Member member : hazelcast.getCluster().getMembers()) {
            String host = member.getAddress().getHost(); // TODO find proper IP
            if (host.equals(ip))
                hazelcast.getExecutorService("ex").executeOnMember(task, member);
        }
    }

    /**
     * Executes the given {@link StreamTask}s on the current cluster.
     * @param tasks the {@link StreamTask}s to execute
     */
    private void submit(Queue<? extends StreamTask> tasks) {
        IExecutorService executorService = hazelcast.getExecutorService("ex");
        Set<Member> members = hazelcast.getCluster().getMembers();

        // Print each machine of the cluster
        members.stream().forEach(System.out::println);

        // TODO Placement constraints
        // Execute task only on machines having the required attributes.

        // TODO Placement optimization
        // Profile network cost (and operator cost) to determine optimal placement.

        // Execute tasks
        StreamTask task;
        while ((task = tasks.poll()) != null)
            executorService.execute(task);
    }

    /**
     * Generators
     */
    String nodePrefix = "t";
    int topicCounter = 0;
    public HazelcastTopic newTopic() {
        return new HazelcastTopic(nodePrefix + "/" + Integer.toString(topicCounter++));
    }
}

