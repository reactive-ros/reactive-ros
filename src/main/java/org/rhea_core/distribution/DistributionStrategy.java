package org.rhea_core.distribution;

import org.rhea_core.Stream;
import org.rhea_core.internal.output.Output;

/**
 * @author Orestis Melkonian
 */
public interface DistributionStrategy {
    int getDesiredGranularity();
    void evaluate(Stream stream, Output output);
}
