package com.snaplogic.otel_poc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify custom attributes for OpenTelemetry spans.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpanAttributes {
    // Array of key-value pairs like {"userId", "123", "requestType", "GET"}
    String[] keyValue() default {};
}
