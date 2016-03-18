package org.rhea_core.distribution;

import java.util.Collections;
import java.util.List;

/**
 * Interface every machine of the cluster should implement.
 * @author Orestis Melkonian
 */
public interface Machine {
    default String hostname() {
        return "localhost";
    }
    default String ip() {
       return "127.0.0.1";
    }
    default int cores() {
        return 1;
    }
    default List<String> skills() {
        return Collections.EMPTY_LIST;
    }
}
