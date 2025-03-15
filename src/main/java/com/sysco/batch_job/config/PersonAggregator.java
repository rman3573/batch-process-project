package com.sysco.batch_job.config;

import org.springframework.batch.item.file.transform.LineAggregator;

public class PersonAggregator implements LineAggregator<String> {
    // In Spring Batch, a LineAggregator is an interface responsible for converting
    // an object into a single, formatted string. It's commonly used when writing
    // data to flat files, where each object needs to be represented as a line of text
    @Override
    public String aggregate(String item) {
        return item;
    }
}
