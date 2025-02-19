package com.snaplogic.otel_poc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation for methods that should not be called on weekends.
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface NotOnWeekends {}
