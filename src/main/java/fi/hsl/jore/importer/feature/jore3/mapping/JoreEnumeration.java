package fi.hsl.jore.importer.feature.jore3.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Indicates that a Java enumeration has been derived from the jr_koodisto table */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface JoreEnumeration {
    /** @return Name of the enumeration (koolista) in jr_koodisto */
    String name() default "";
}
