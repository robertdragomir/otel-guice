package com.snaplogic.service;

import com.snaplogic.otel_poc.SpanAttributes;
import com.snaplogic.otel_poc.Traced;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import io.opentelemetry.extension.annotations.WithSpan;

// The @Traced annotation is used to mark classes that should be intercepted by the TracingInterceptor.
// The @TracedAttribute annotation is used to mark methods that should have attributes added to the span.
// The TracingInterceptor is bound in the TracingModule.
// Note that any method in the class will be intercepted (otel tracing)


public class UserService {

    public void getUserDetails() {
        System.out.println("UserService.getUserDetails.. with span attributes");
    }

    @Traced
    public void getStuff() {
        System.out.println("UserService.getStuff.");
        Span currentSpan = Span.current();
        Baggage baggage = Baggage.current();
        baggage.forEach((key, entry) -> {
            currentSpan.setAttribute(key, entry.getValue());
        });

    }

    @Traced
    @SpanAttributes(keyValue = {"userId", "123", "requestType", "GET"})
    public void getUserDetails2() {

        Span currentSpan = Span.current();

        currentSpan.setAttribute("custom-attribute", "custom-value");
        currentSpan.setAttribute("custom-attribute2", "custom-value2");

        Baggage baggage = Baggage.builder()
                .put("custom-attribute", "custom-value")
                .put("custom-attribute2", "custom-value2")
                .build();
        try (Scope scope = baggage.makeCurrent()) {
            // The baggage is now set in the current context and will be propagated with the span
            getStuff();
        }
        System.out.println("UserService.getUserDetails2.. with span attributes");
    }
}
