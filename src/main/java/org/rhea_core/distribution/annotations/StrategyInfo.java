package org.rhea_core.distribution.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Orestis Melkonian
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrategyInfo {
    String id() default "";
    boolean distributed() default false;
    String[] requiredSkills() default "";
    int priority() default 0;
}
