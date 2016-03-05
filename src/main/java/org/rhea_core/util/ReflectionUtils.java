package org.rhea_core.util;

import org.rhea_core.distribution.annotations.MachineInfo;
import org.rhea_core.distribution.annotations.StrategyInfo;
import org.rhea_core.distribution.Machine;
import org.rhea_core.evaluation.EvaluationStrategy;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Used to parse annotations using Java reflection.
 * @author Orestis Melkonian
 */
public final class ReflectionUtils {
    static Reflections reflections = new Reflections();

    public static StrategyInfo getTopStrategy() {
        Optional<StrategyInfo> strategy =
                reflections.getSubTypesOf(EvaluationStrategy.class)
                        .stream()
                        .filter(c -> !c.isInterface() && !Modifier.isAbstract(c.getModifiers()))
                        .map(c -> c.getAnnotation(StrategyInfo.class))
                        .sorted((s1, s2) -> s1.priority() > s2.priority() ? 1 : (s1.priority() == s2.priority()) ? 0 : -1)
                        .findFirst();

        if (strategy.isPresent())
            return strategy.get();
        else
            throw new RuntimeException("No EvaluationStrategy in classpath");
    }

    public static List<StrategyInfo> getStrategies() {
        return reflections.getSubTypesOf(EvaluationStrategy.class)
                .stream()
                .filter(c -> !c.isInterface() && !Modifier.isAbstract(c.getModifiers()))
                .map(c -> c.getAnnotation(StrategyInfo.class))
                .collect(Collectors.toList());
    }

    public static List<MachineInfo> getMachines() {
        return reflections.getSubTypesOf(Machine.class)
                .stream()
                .filter(c -> !c.isInterface() && !Modifier.isAbstract(c.getModifiers()))
                .map(c -> c.getAnnotation(MachineInfo.class))
                .collect(Collectors.toList());
    }
}
