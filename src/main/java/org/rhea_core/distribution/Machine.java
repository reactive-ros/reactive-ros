package org.rhea_core.distribution;

import java.util.ArrayList;
import java.util.List;

/**
 * Class providing information about a machine that will join the cluster.
 * @author Orestis Melkonian
 */
public class Machine {

    private String ip;
    private String name = "default";
    private List<String> attributes = new ArrayList<>();

    public Machine(String ip, String name, List<String> attributes) {
        this.ip = ip;
        this.name = name;
        this.attributes = attributes;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        String ret = "IP: " + ip;
        ret += "Name: " + name;
        ret += "Attr: " + attributes;
        return ret;
    }
}
