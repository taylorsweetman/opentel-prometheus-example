package org.example;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;

public class OpenTelemetryMetrics {
    private final String jobName;
    private final String stepName;
    private LongHistogram histogram;
    private final Meter meter;
    private LongCounter counter;
    private long startTime;

    public OpenTelemetryMetrics(OpenTelemetry openTelemetry, String jobName, String stepName) {
        this.jobName = jobName;
        this.stepName = stepName;

        this.meter = openTelemetry.getMeter(jobName);
    }

    private void initHistogram() {
        this.histogram = meter
                .histogramBuilder(stepName + ".histogram")
                .ofLongs()
                .setDescription("Histogram for " + jobName)
                .setUnit("ms")
                .build();
    }

    private void initCounter() {
        this.counter = meter
                .counterBuilder(stepName + ".counter")
                .setDescription("Counter for " + jobName)
                .build();
    }

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        if (histogram == null) initHistogram();

        histogram.record(System.currentTimeMillis() - startTime);
    }

    public void incrementCounter(long number) {
        if (counter == null) initCounter();

        counter.add(number);
    }
}
