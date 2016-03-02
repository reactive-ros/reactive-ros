package org.rhea_core.distribution;

/**
 * @author Orestis Melkonian
 */
public class Broker {
    String ip;
    int port;

    public Broker(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "tcp://" + ip + ":" + port;
    }
}
