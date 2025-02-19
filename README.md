# Otel Guice Bindings POC
Test Program for illustrating otel bindings behind Google Guice bindings.

## To Build

To build the program, type the following on the commandline. This will produce a jar file in the target directory:
> mvn clean compile assembly:single

## To Run

To execute the program, type the following on the commandline:
> java -cp target/otel-poc-1.0-SNAPSHOT-jar-with-dependencies.jar com.snaplogic.Main

## Expected Output

Upon successful output, you should see something similar like the below:
```
TracingInterceptor.invoke with spanName=MyService$$EnhancerByGuice$$455191.doSomething MyService.doSomething.
TracingInterceptor.invoke with spanName=MyService$$EnhancerByGuice$$455191.doSomething2
MyService.doSomething2.
TracingInterceptor.invoke with spanName=UserService$$EnhancerByGuice$$1987429.getUserDetails
TracingInterceptor.invoke--span attr=user.id value=12345
UserService.getUserDetails.. with span attributes
TracingInterceptor.invoke with spanName=UserService$$EnhancerByGuice$$1987429.getStuff
UserService.getStuff.
TracingInterceptor.invoke with spanName=UserService$$EnhancerByGuice$$1987429.getUserDetails2
TracingInterceptor.invoke--span attr=user.id2 value=123456789
UserService.getUserDetails2.. with span attributes
```

Key points in the above output are as follows:
* **"TracingInterceptor.invoke"** - printed from injected otel code with span name.
* **"TracingInterceptor.invoke--span attr"** - printed from injected otel code with span name + span attributes.
