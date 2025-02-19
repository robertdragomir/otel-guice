package com.snaplogic.otel_poc;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.snaplogic.service.MyService;


/**
 * Guice module for blocking calls on weekends.
 */
public class NotOnWeekendsModule extends AbstractModule {

    @Override
    protected void configure() {
        System.out.println("NotOnWeekendsModule: configure");
        WeekendBlocker weekendBlocker = new WeekendBlocker();
        requestInjection(weekendBlocker);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(NotOnWeekends.class),
                weekendBlocker);
        bind(MyService.class);
    }
}

