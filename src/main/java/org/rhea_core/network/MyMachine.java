package org.rhea_core.network;

import org.rhea_core.network.Machine;

import java.util.Arrays;
import java.util.List;

/**
 * @author Orestis Melkonian
 */
public class MyMachine implements Machine {
    @Override
    public int cores() {
        return 4;
    }

    @Override
    public List<String> skills() {
        return Arrays.asList("ros", "mqtt");
    }
}