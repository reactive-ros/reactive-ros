package org.rhea_core.distribution.annotations.machines;

import org.rhea_core.distribution.Machine;
import org.rhea_core.distribution.annotations.MachineInfo;

/**
 * @author Orestis Melkonian
 */
@MachineInfo(skills = {"Ros", "Mqtt"}, cores = 4)
public class MyMachine implements Machine {
}
