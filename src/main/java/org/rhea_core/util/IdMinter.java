package org.rhea_core.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that generates unique identifiers in a thread-safe manner.
 * @author Orestis Melkonian
 */
public final class IdMinter {

    static final AtomicInteger gen = new AtomicInteger(0);

    public static int next() {
        return gen.getAndIncrement();
    }
}
