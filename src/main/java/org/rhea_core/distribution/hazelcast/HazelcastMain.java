package org.rhea_core.distribution.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.MemberAttributeConfig;
import com.hazelcast.core.Hazelcast;

/**
 * @author Orestis Melkonian
 */
public class HazelcastMain {
    public static void init(Config config) {
        Hazelcast.newHazelcastInstance(config);
    }
}
