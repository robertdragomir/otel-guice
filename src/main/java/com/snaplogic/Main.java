package com.snaplogic;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.snaplogic.otel_poc.NotOnWeekends;
import com.snaplogic.otel_poc.NotOnWeekendsModule;
import com.snaplogic.otel_poc.TracingModule;
import com.snaplogic.service.MyService;
import com.snaplogic.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        OpenTelemetryConfig.initOpenTelemetry(); // Initialize OpenTelemetry

        Main main = new Main();
        Injector injector = main.initInjector();

        // MyService class demonstrates various tracing techniques
        // including OpenTelemetry built-in annotations and custom Guice annotations.
        MyService myService = injector.getInstance(MyService.class);
        myService.doSomething();
        myService.doSomething2();
        myService.doSomething3();
        myService.doSomething4("Hi", 123);

        // UserService class demonstrates various tracing techniques
        // including OpenTelemetry built-in annotations and custom Guice annotations.
        UserService userService = injector.getInstance(UserService.class);
        userService.getUserDetails();
        userService.getStuff();
        // Create a supplier for attributes
        Supplier<Map<String, Object>> attributeSupplier = () -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("userId", "123");
            attributes.put("requestType", "GET");
            attributes.put("payload", "Hello");
            return attributes;
        };

        // Call getUserDetails2 with the attribute supplier
        userService.getUserDetails2(attributeSupplier);
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
