package org.rhea_core.distribution.tasks;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import org.rhea_core.Stream;
import org.rhea_core.distribution.hazelcast.HazelcastTopic;
import org.rhea_core.evaluation.EvaluationStrategy;
import org.rhea_core.internal.output.Output;
import org.rhea_core.util.functions.Func0;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class StreamTask implements Runnable, Serializable, HazelcastInstanceAware {

    private HazelcastInstance hazelcast;

    protected Func0<EvaluationStrategy> strategyGenerator;
    protected Stream stream;
    protected Output output;
    private List<String> requiredAttributes;

    public StreamTask(Func0<EvaluationStrategy> strategyGenerator, Stream stream, Output output) {
        this.strategyGenerator = strategyGenerator;
        this.stream = stream;
        this.output = output;
        requiredAttributes = new ArrayList<>();
    }

    public StreamTask(Func0<EvaluationStrategy> strategyGenerator, Stream stream, Output output, List<String> requiredAttributes) {
        this.strategyGenerator = strategyGenerator;
        this.stream = stream;
        this.output = output;
        this.requiredAttributes = requiredAttributes;
    }

    public List<String> getRequiredAttributes() {
        return requiredAttributes;
    }

    public Output getOutput() {
        return output;
    }

    public Stream getStream() {
        return stream;
    }

    public Func0<EvaluationStrategy> getStrategyGenerator() {
        return strategyGenerator;
    }

    public HazelcastInstance getHazelcast() {
        return hazelcast;
    }

    @Override
    public void run() {
        System.out.println("--------------------------" + instanceIP(hazelcast) + "--------------------------");

        // Setup network configuration TODO move to init() main method
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

        for (HazelcastTopic topic : HazelcastTopic.extract(stream, output))
            topic.setClient(hazelcast);

        strategyGenerator.call().evaluate(stream, output);
    }

    @Override
    public String toString() {
        return "\n\n======================== "
                + ManagementFactory.getRuntimeMXBean().getName() + "@" + Thread.currentThread().getId()
                + " ========================"
                + "\n" + stream.getGraph()
                + "\n\t===>\t" + output + "\n"
                + "\n==================================================\"\n\n";
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcast = hazelcastInstance;
    }

    private String instanceIP(HazelcastInstance instance) {
        String str = instance.toString();
        return (str.substring(str.indexOf("Address") + 7, str.length() - 1)).replace("[","").replace("]","");
    }
}