package com.snaplogic.otel_poc;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.snaplogic.service.MyService;
import com.snaplogic.service.UserService;

public class TracingModule extends AbstractModule {
    @Override
    protected void configure() {

        System.out.println("NotOnWeekendsModule: configure");
        TracingInterceptor spanInterceptor = new TracingInterceptor();
        requestInjection(spanInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Traced.class),
                spanInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(SpanAttributes.class),
                spanInterceptor);

        bind(MyService.class);
        bind(UserService.class);
    }
}
