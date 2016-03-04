package org.rhea_core.distribution;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import javafx.collections.transformation.SortedList;
import org.reflections.Reflections;
import org.rhea_core.Stream;
import org.rhea_core.distribution.annotations.MachineInfo;
import org.rhea_core.evaluation.EvaluationStrategy;
import org.rhea_core.distribution.annotations.StrategyInfo;
import org.rhea_core.internal.output.Output;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


public class Distributor {
    HazelcastInstance hazelcast;
    List<MachineInfo> machines = new ArrayList<>();
    List<StrategyInfo> strategies = new ArrayList<>();
    StrategyInfo distributedStrategy;
    int desiredGranularity;

    public Distributor() {
        Reflections ref = new Reflections();

        // Find machines at classpath
        System.out.println("----------------------- MACHINES -----------------------");
        List<Class<? extends Machine>> machineClasses =
                ref.getSubTypesOf(Machine.class).stream()
                        .filter(c -> !c.isInterface() && !Modifier.isAbstract(c.getModifiers()))
                        .collect(Collectors.toList());
        for (Class clazz : machineClasses) {
            MachineInfo machine = (MachineInfo) clazz.getAnnotation(MachineInfo.class);
            System.out.println("hostname: " + machine.hostname());
            System.out.println("ip: " + machine.ip());
            System.out.println("skills: " + Arrays.toString(machine.skills()));
            System.out.println();

            machines.add(machine);
        }
        desiredGranularity = machines.stream().map(MachineInfo::cores).reduce((i1, i2) -> i1 + i2).get();

        System.out.println("----------------------- STRATEGIES -----------------------");
        // Find strategies at classpath
        List<Class<? extends EvaluationStrategy>> strategyClasses =
                ref.getSubTypesOf(EvaluationStrategy.class)
                        .stream()
                        .filter(c -> !c.isInterface() && !Modifier.isAbstract(c.getModifiers()))
                        .collect(Collectors.toList());

        List<StrategyInfo> distStrategies = new ArrayList<>();
        for (Class clazz : strategyClasses) {
            StrategyInfo strategy = (StrategyInfo) clazz.getAnnotation(StrategyInfo.class);
            System.out.println("id: " + strategy.id());
            System.out.println("distributed: " + strategy.distributed());
            System.out.println("requiredSkills: " + Arrays.toString(strategy.requiredSkills()));
            System.out.println("priority: " + strategy.priority());
            System.out.println();

            strategies.add(strategy);
            if (strategy.distributed())
                distStrategies.add(strategy);
        }

        // Check that at least 1 distributed evaluation strategy is present
        if (distStrategies.isEmpty())
            throw new RuntimeException("No distributed evaluation strategy in classpath");

        // Set distributed strategy with highest priority
        Collections.sort(distStrategies, (s1, s2) -> s1.priority() > s2.priority() ? 1 : (s1.priority() == s2.priority()) ? 0 : -1);
        distributedStrategy = distStrategies.get(0);

        hazelcast = Hazelcast.newHazelcastInstance();

        // Setup network configuration
        /*List<String> addresses = machines.stream().map(MachineInfo::hostname).collect(Collectors.toList());
        Config cfg = new Config();
        NetworkConfig network = cfg.getNetworkConfig();
        network.setReuseAddress(true);

        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        TcpIpConfig ipConfig = join.getTcpIpConfig().setEnabled(true);
        for (String address : addresses)
            ipConfig = ipConfig.addMember(address);
        InterfacesConfig interfaces = network.getInterfaces().setEnabled(true);
        for (String address : addresses)
            interfaces = interfaces.addInterface(address);
        hazelcast = Hazelcast.newHazelcastInstance(cfg);*/
    }

    public int getDesiredGranularity() {
        return desiredGranularity;
    }

    public void evaluate(Stream stream, Output output) {





        // TODO decrease granularity
        if (stream.getGraph().size() > desiredGranularity) {
        }

        // Execute
    }

    public void executeOn(Runnable task, String ip) {
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
    public void submit(Queue<? extends StreamTask> tasks) {
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
}

