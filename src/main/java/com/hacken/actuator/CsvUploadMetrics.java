package com.hacken.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CsvUploadMetrics {

    private final Counter csvUploadCounter;

    public CsvUploadMetrics(MeterRegistry meterRegistry) {
        this.csvUploadCounter = meterRegistry.counter("csv.uploads",
                "type", "upload",
                "description", "Counts the number of CSV file uploads");
    }

    public void incrementCsvUpload() {
        csvUploadCounter.increment();
    }
}
