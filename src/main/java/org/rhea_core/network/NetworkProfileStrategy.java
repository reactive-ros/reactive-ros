package org.rhea_core.network;

import java.util.Set;
import java.util.Map;
import org.javatuples.Pair;

/**
 * @author Orestis Melkonian
 * @param <D> the data type representing the distance (i.e. network cost)
 */
public interface NetworkProfileStrategy<D> {
    Map<Pair<Machine, Machine>, D> profile(Set<Machine> machines);
}
