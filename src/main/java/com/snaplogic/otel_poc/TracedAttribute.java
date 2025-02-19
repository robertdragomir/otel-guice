package com.snaplogic.otel_poc;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TracedAttribute {
    String key();
    String value();
}
