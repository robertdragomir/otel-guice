package com.snaplogic.service;

import com.snaplogic.otel_poc.NotOnWeekends;
import com.snaplogic.otel_poc.SpanAttributes;
import com.snaplogic.otel_poc.Traced;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.extension.annotations.SpanAttribute;
import io.opentelemetry.extension.annotations.WithSpan;

/**
 * MyService class demonstrates various tracing techniques
 * including OpenTelemetry built-in annotations and custom Guice annotations.
 */
public class MyService {

    /**
     * This method is traced and has custom attributes.
     * The @Traced Guice annotation is used to mark methods
     * that should be intercepted by the TracingInterceptor.
     * The @SpanAttributes Guice annotation is used to specify custom attributes for the span.
     */
    @Traced
    @SpanAttributes(keyValue = {"userId", "123", "requestType", "GET"})
    public void doSomething() {
        System.out.println("MyService.doSomething.");
    }

    /**
     * This method uses dummy Guice annotation for testing.
     */
    @NotOnWeekends
    public void doSomething2() {
        System.out.println("MyService.doSomething2.");
    }

    /**
     * This method demonstrates manual OTel span creation.
     */
    public void doSomething3() {
        Tracer tracer = GlobalOpenTelemetry.getTracer("MyApp");
        Span span = tracer.spanBuilder("manualSpan").startSpan();
        try (Scope scope = span.makeCurrent()) {
            System.out.println("MyService.doSomething3.");
        } finally {
            span.end();
        }
    }

    /**
     * This method demonstrates the use of @WithSpan built-in OTel annotation
     * and manually setting attributes and baggage.
     */
    @WithSpan
    public void doSomething4(@SpanAttribute("attribute1") String att1,
                             @SpanAttribute("attribute2") int att2) {
        System.out.println("MyService.doSomething4.");

        Span currentSpan = Span.current();

        currentSpan.setAttribute("custom-attribute", "custom-value");
        currentSpan.setAttribute("custom-attribute2", "custom-value2");

        Baggage baggage = Baggage.builder()
                .put("custom-attribute", "custom-value")
                .put("custom-attribute2", "custom-value2")
                .build();
        try (Scope scope = baggage.makeCurrent()) {
            doSomething5();
        }
    }

    /**
     * This method demonstrates the use of @Traced Guice annotation.
     */
    @Traced
    public void doSomething5() {
        System.out.println("MyService.doSomething5.");
        doSomething6();
    }

    /**
     * This method demonstrates the use of @WithSpan built-in OTel annotation
     * and manually setting attributes extracting the information from  baggage.
     */
    @WithSpan
    public void doSomething6() {
        System.out.println("MyService.doSomething6.");
        Span currentSpan = Span.current();
        Baggage baggage = Baggage.current();
        baggage.forEach((key, entry) -> {
            currentSpan.setAttribute(key, entry.getValue());
        });
    }
}
