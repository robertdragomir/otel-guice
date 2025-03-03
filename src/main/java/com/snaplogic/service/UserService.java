package com.snaplogic.service;

import com.snaplogic.otel_poc.Traced;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;

import java.util.Map;
import java.util.function.Supplier;

/**
 * UserService class demonstrates various tracing techniques
 * including OpenTelemetry built-in annotations and custom Guice annotations.
 */
public class UserService {

    public void getUserDetails() {
        System.out.println("UserService.getUserDetails.. with span attributes");
    }

    /**
     * This method demonstrates the use of @Traced Guice annotation
     * and manually setting attributes from baggage extraction.
     */
    @Traced
    public void getStuff() {
        System.out.println("UserService.getStuff.");
        Span currentSpan = Span.current();
        Baggage baggage = Baggage.current();
        baggage.forEach((key, entry) -> {
            currentSpan.setAttribute(key, entry.getValue());
        });

    }

    /**
     * This method demonstrates the use of @Traced Guice annotation
     * and manually setting attributes and injecting baggage.
     */
    @Traced
    public void getUserDetails2(Supplier<Map<String, Object>> attributeSupplier) {
        String userId = "12345";
        String requestType = "GET55";

        Span currentSpan = Span.current();

        if (attributeSupplier != null) {
            Map<String, Object> attributes = attributeSupplier.get();
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                currentSpan.setAttribute(entry.getKey(), entry.getValue().toString());
            }
        }

        currentSpan.setAttribute("custom-attribute", "custom-value");
        currentSpan.setAttribute("custom-attribute2", "custom-value2");

        Baggage baggage = Baggage.builder()
                .put("custom-attribute", "custom-value")
                .put("custom-attribute2", "custom-value2")
                .build();
        try (Scope scope = baggage.makeCurrent()) {
            // The baggage is now set in the current context and will be propagated with the span
            intermediateMethod();
        }
        System.out.println("UserService.getUserDetails2.. with span attributes");
    }

    /**
     * This method demonstrates the use of @Traced Guice annotation.
     */
    @Traced
    public void intermediateMethod() {
        System.out.println("UserService.intermediateMethod.");
        getStuff();
    }
}
