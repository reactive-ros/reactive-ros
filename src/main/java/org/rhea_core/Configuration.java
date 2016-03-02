package org.rhea_core;

import org.rhea_core.distribution.Distributor;
import org.rhea_core.evaluation.EvaluationStrategy;

/**
 * @author Orestis Melkonian
 */
public class Configuration {
    public EvaluationStrategy strategy;
    public Distributor remote;

    public Configuration(EvaluationStrategy strategy, Distributor remote) {
        this.strategy = strategy;
        this.remote = remote;
    }
}
