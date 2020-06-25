package com.workday.techtest.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Report {
    private long numInvocations;
    private long numberOfUniqueJobsProcessed;
    private long totalExecutionTimeInMilliseconds;

    @Override
    public String toString() {
        return "Total number of invocations: " + numInvocations + "\n" +
                "Unique jobs processed: " + numberOfUniqueJobsProcessed + "\n" +
                "Total execution time in milliseconds: " + totalExecutionTimeInMilliseconds + "\n";
    }
}
