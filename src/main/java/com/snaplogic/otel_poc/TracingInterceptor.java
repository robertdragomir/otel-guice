package com.snaplogic.otel_poc;

import com.google.inject.Singleton;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;


/**
 * Interceptor for creating OpenTelemetry spans and setting attributes.
 */
@Singleton
public class TracingInterceptor implements MethodInterceptor {

    private static final Tracer tracer = GlobalOpenTelemetry.getTracer("JCC");

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // Get the method name and the annotations on the method
        String spanName = invocation.getMethod().getName();

        // Start a new span
        Span span = tracer.spanBuilder(spanName).startSpan();

        // Check for @SpanAttributes annotation and set attributes if present
        Annotation[] annotations = invocation.getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof SpanAttributes) {
                SpanAttributes spanAttributes = (SpanAttributes) annotation;
                String[] keyValue = spanAttributes.keyValue();
                for (int i = 0; i < keyValue.length; i += 2) {
                    String key = keyValue[i];
                    String value = keyValue[i + 1];
                    span.setAttribute(key, value);
                }
            }
        }

        // Set the span in the context so that it can be tracked during the method execution
        try (Scope scope = span.makeCurrent()) {
            // Proceed with the method execution
            return invocation.proceed();
        } catch (Exception e) {
            span.recordException(e);
            throw e;
        } finally {
            // End the span when method execution finishes
            span.end();
        }
    }
}
