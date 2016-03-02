package org.rhea_core.distribution;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;


public class RemoteExecution {
    HazelcastInstance hazelcast;
    List<Machine> machines;

    /**
     * Default constructor for single-machine usage.
     */
    public RemoteExecution() {
        hazelcast = Hazelcast.newHazelcastInstance();
        machines = new ArrayList<>();
    }

    /**
     * Constructor: should be executed on every machine with the same cluster information.
     * @param machines the machines to use for task distribution
     */
    public RemoteExecution(List<Machine> machines) {
        this.machines = machines;
        List<String> addresses = machines.stream().map(Machine::getIp).collect(Collectors.toList());

        // Setup network configuration
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

        hazelcast = Hazelcast.newHazelcastInstance(cfg);
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

        // TODO Placement constrains
        // Execute task only on machines having the required attributes.

        // TODO Placement optimization
        // Profile network cost and operator cost to determine optimal placement.

        // Execute tasks
        StreamTask task;
        while ((task = tasks.poll()) != null)
            executorService.execute(task);
    }

    public int getDesiredGranularity() {
        int totalCores = 0;
        for (Member member : hazelcast.getCluster().getMembers()) {
            int cores = member.getIntAttribute("CPU_CORES");
            totalCores += cores;
        }
        return totalCores;
    }
}

