package org.example;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Example of using the PrometheusHttpServer to convert OTel metrics to Prometheus format and expose
 * these to a Prometheus instance via a HttpServer exporter.
 *
 * <p>A Gauge is used to periodically measure how many incoming messages are awaiting processing.
 * The Gauge callback gets executed every collection interval.
 */
public final class PrometheusExample {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        var prometheusPort = Integer.parseInt(dotenv.get("PROMETHEUS_PORT", "19090"));

        // it is important to initialize your SDK as early as possible in your application's lifecycle
        OpenTelemetry openTelemetry = ExampleConfiguration.initOpenTelemetry(prometheusPort);

        Tracer tracer = openTelemetry.getTracer("io.opentelemetry.example.prometheus");
        OpenTelemetryMetrics metrics = new OpenTelemetryMetrics(openTelemetry, "etl.example", "all.steps");

        for (int i = 0; i < 500; i++) {
            metrics.startTimer();
            Span exampleSpan = tracer.spanBuilder("exampleSpan").startSpan();
            Context exampleContext = Context.current().with(exampleSpan);
            try (Scope _ignored = exampleContext.makeCurrent()) {
                metrics.incrementCounter(1);
                exampleSpan.setAttribute("good", true);
                exampleSpan.setAttribute("exampleNumber", i);
                ETLJob.run();
            } finally {
                metrics.stopTimer();
                exampleSpan.end();
            }
        }

        System.out.println("Exiting");
    }
}