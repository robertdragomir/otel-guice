package com.snaplogic.service;

import com.snaplogic.otel_poc.NotOnWeekends;
import com.snaplogic.otel_poc.SpanAttributes;
import com.snaplogic.otel_poc.Traced;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.extension.annotations.SpanAttribute;
import io.opentelemetry.extension.annotations.WithSpan;

// The @Traced annotation is used to mark classes that should be intercepted by the TracingInterceptor.
// The TracingInterceptor is bound in the TracingModule.
// Note that any method in the class will be intercepted (otel tracing)


public class MyService {
    @Traced
    @SpanAttributes(keyValue = {"userId", "123", "requestType", "GET"})
    public void doSomething() {
        System.out.println("MyService.doSomething.");
    }
    @NotOnWeekends
    public void doSomething2() {
        System.out.println("MyService.doSomething2.");
    }
    public void doSomething3() {
        Tracer tracer = GlobalOpenTelemetry.getTracer("MyApp");
        Span span = tracer.spanBuilder("manualSpan").startSpan();
        try (Scope scope = span.makeCurrent()) {
            System.out.println("MyService.doSomething3.");
        } finally {
            span.end();
        }
    }

    @WithSpan
    public void doSomething4(@SpanAttribute("attribute1") String att1,
                             @SpanAttribute("attribute2") int att2) {
        System.out.println("MyService.doSomething4.");
    }
}
