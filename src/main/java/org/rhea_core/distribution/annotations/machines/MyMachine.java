package org.rhea_core.distribution.annotations.machines;

import org.rhea_core.distribution.Machine;
import org.rhea_core.distribution.annotations.MachineInfo;

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