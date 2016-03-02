package org.rhea_core.distribution;

import java.io.Serializable;

/**
 * @author Orestis Melkonian
 */
public abstract class BrokerTask implements Runnable, Serializable {
    protected Broker broker;

    public BrokerTask(Broker broker) {
        this.broker = broker;
    }
}
