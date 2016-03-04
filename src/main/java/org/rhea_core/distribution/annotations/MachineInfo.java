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
public @interface MachineInfo {
    String hostname() default "localhost";
    String ip() default "127.0.0.1";
    int cores() default 1;
    String[] skills() default "";
}
