package com.snaplogic.otel_poc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Interceptor for blocking calls on weekends.
 */
public class WeekendBlocker implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("WeekendBlocker: Interceptor invoked for method: " +
                invocation.getMethod().getName());

        DayOfWeek currentDay = LocalDate.now().getDayOfWeek();

        if (currentDay == DayOfWeek.SATURDAY || currentDay == DayOfWeek.SUNDAY) {
            System.out.println("WeekendBlocker: Blocking method invocation on weekend: " +
                    invocation.getMethod().getName());
            throw new Throwable("Method invocation not allowed on weekends!");
        } else {
            System.out.println("WeekendBlocker: Method invocation allowed on weekday: " +
                    invocation.getMethod().getName());
        }
        return invocation.proceed();
    }
}
