package org.rhea_core.distribution;

import com.hazelcast.config.Config;
import com.hazelcast.config.MemberAttributeConfig;
import com.hazelcast.core.Hazelcast;

/**
 * @author Orestis Melkonian
 */
public class HazelcastMain {
    public static void init(Config config) {
        // Add #CPU_CORES attribute
        MemberAttributeConfig memberConfig = new MemberAttributeConfig();
        memberConfig.setIntAttribute("CPU_CORES", Runtime.getRuntime().availableProcessors());
        config.setMemberAttributeConfig(memberConfig);

        Hazelcast.newHazelcastInstance(config);
    }
}
