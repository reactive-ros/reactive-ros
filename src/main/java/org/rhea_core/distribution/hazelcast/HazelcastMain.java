package org.rhea_core.distribution.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;

/**
 * @author Orestis Melkonian
 */
public class HazelcastMain {
    public static void init(Config config) {
        Hazelcast.newHazelcastInstance(config);

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
}
