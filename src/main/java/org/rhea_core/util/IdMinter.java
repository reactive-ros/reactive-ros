package org.rhea_core.util;

import org.omg.CORBA.Object;
import org.rhea_core.util.functions.Function;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that generates unique identifiers in a thread-safe manner.
 * @author Orestis Melkonian
 */
public final class IdMinter {

    static int current_id = 0;
    static final Lock lock = new ReentrantLock();

    public static int next() {
        synchronized (lock) {
            return current_id++;
        }
    }
}
