package com.workday.techtest.executors;

public interface JobRunner {

    void runner(JobQueue jobQueue, long jobCount);

    String version();
}
