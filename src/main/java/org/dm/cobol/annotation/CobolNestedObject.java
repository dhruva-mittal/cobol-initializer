package org.dm.cobol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as a nested COBOL structure.
 * Fields with this annotation will be recursively processed during
 * initialization and parsing operations.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CobolNestedObject {
    /**
     * Optional description of the nested structure
     * 
     * @return Structure description
     */
    String description() default "";
    
    /**
     * Whether this field is optional
     * 
     * @return True if the field is optional, false otherwise
     */
    boolean optional() default false;
}