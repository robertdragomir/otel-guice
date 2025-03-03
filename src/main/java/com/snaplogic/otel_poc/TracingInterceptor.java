package com.snaplogic.otel_poc;

import com.google.inject.Singleton;

import java.util.Map;
import java.util.function.Supplier;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * Interceptor for creating OpenTelemetry spans and setting attributes.
 */
@Singleton
public class TracingInterceptor implements MethodInterceptor {

    private static final Tracer tracer = GlobalOpenTelemetry.getTracer("JCC");

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String spanName = invocation.getMethod().getName();
        Span span = tracer.spanBuilder(spanName).startSpan();

        // Extracting custom attributes from the SpanAttributes annotation
        Supplier<Map<String, Object>> attributeSupplier =
                extractAttributeSupplier(invocation.getArguments());

        // If the method has SpanAttributes annotation, use it to set attributes
        if (attributeSupplier != null) {
            Map<String, Object> attributes = attributeSupplier.get();
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                span.setAttribute(entry.getKey(), entry.getValue().toString());
            }
        }

        try (Scope scope = span.makeCurrent()) {
            return invocation.proceed();
        } catch (Throwable t) {
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }
    }

    private Supplier<Map<String, Object>> extractAttributeSupplier(Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Supplier) {
                return (Supplier<Map<String, Object>>) arg;
            }
        }
        return () -> Map.of(); // Return an empty map if no supplier is found
    }
}
