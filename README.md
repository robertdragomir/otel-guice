# Otel Guice Bindings POC
Test Program for illustrating otel bindings behind Google Guice bindings.

## To Build

To build the program, type the following on the commandline. This will produce a jar file in the target directory:
> mvn clean compile assembly:single

## To Run

To execute the program, type the following on the commandline:
> java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
 -javaagent:otel-javaagent.jar \
 -Dotel.exporter.otlp.endpoint=http://localhost:4317 \
 -Dotel.exporter.otlp.protocol=grpc \
 -Dotel.service.name=my-service \
 -jar target/otel-guice-poc-1.0-SNAPSHOT-jar-with-dependencies.jar

## Observations
The OpenTelemetry collector is running in the background, collecting and exporting traces over the OTLP protocol to localhost:4317, which is where your OpenTelemetry Java agent is configured to send the trace data.
You are using Jaeger to visualize the collected traces, which allows you to see detailed insights into the behavior and performance of your services as they interact.

## Expected Output

Upon successful output, you should see something similar like the below:
```
Listening for transport dt_socket at address: 5005
[otel.javaagent 2025-02-19 14:57:41:197 -0800] [main] INFO io.opentelemetry.javaagent.tooling.VersionLogger - opentelemetry-javaagent - version: 2.13.0
[otel.javaagent 2025-02-19 14:57:43:454 -0800] [main] WARN io.opentelemetry.api.GlobalOpenTelemetry - You are currently using the OpenTelemetry Instrumentation Java Agent; all GlobalOpenTelemetry.set calls are ignored - the agent provides the global OpenTelemetry object used by your application.
java.lang.Throwable
	at io.opentelemetry.api.GlobalOpenTelemetry.set(GlobalOpenTelemetry.java:97)
	at com.snaplogic.OpenTelemetryConfig.initOpenTelemetry(OpenTelemetryConfig.java:26)
	at com.snaplogic.Main.main(Main.java:13)
NotOnWeekendsModule: configure
NotOnWeekendsModule: configure
MyService.doSomething.
WeekendBlocker: Interceptor invoked for method: doSomething2
WeekendBlocker: Method invocation allowed on weekday: doSomething2
MyService.doSomething2.
MyService.doSomething3.
MyService.doSomething4.
UserService.getUserDetails.. with span attributes
UserService.getStuff.
UserService.getStuff.
UserService.getUserDetails2.. with span attributes
Call annotation
Done with user service
```

Key points in the above output are as follows:
* **"TracingInterceptor.invoke"** - printed from injected otel code with span name.
* **"TracingInterceptor.invoke--span attr"** - printed from injected otel code with span name + span attributes.
