package com.snaplogic;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.snaplogic.otel_poc.NotOnWeekends;
import com.snaplogic.otel_poc.NotOnWeekendsModule;
import com.snaplogic.otel_poc.TracingModule;
import com.snaplogic.service.MyService;
import com.snaplogic.service.UserService;

public class Main {
    public static void main(String[] args) {
        OpenTelemetryConfig.initOpenTelemetry(); // Initialize OpenTelemetry

        Main main = new Main();
        Injector injector = main.initInjector();

        // sample class with @Traced annotation at the class level
        MyService myService = injector.getInstance(MyService.class);
        myService.doSomething();
        myService.doSomething2();
        myService.doSomething3();
        myService.doSomething4("Hi", 123);

        // sample class with @Traced annotation at the class and method level (w/ attributes)
        UserService userService = injector.getInstance(UserService.class);
        userService.getUserDetails();
        userService.getStuff();
        userService.getUserDetails2();
        main.callAnnotation();
        System.out.println("Done with user service");
    }

    private Injector initInjector() {
        // set up the guice injector
        return Guice.createInjector(new NotOnWeekendsModule(), new TracingModule());
    }

    @NotOnWeekends
    public void callAnnotation() {
        System.out.println("Call annotation");
    }
}
