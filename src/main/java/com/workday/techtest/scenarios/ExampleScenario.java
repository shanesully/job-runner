package com.workday.techtest.scenarios;

import com.workday.techtest.domain.Job;
import com.workday.techtest.domain.Report;
import com.workday.techtest.executors.JobQueue;
import com.workday.techtest.executors.JobQueueImpl;
import com.workday.techtest.executors.JobRunnerImpl;

import java.util.Arrays;
import java.util.List;

public class ExampleScenario {

    private static final int jobCount;
    private static final JobQueue queue;
    private static final JobRunnerImpl runner;
    private static final Report report;

    static {
        jobCount = 3;
        queue = new JobQueueImpl(createExampleJobs());
        runner = new JobRunnerImpl();
        report = new Report();
    }

    private ExampleScenario() {
    }

    public static void run() {
        runner.runner(queue, jobCount);
    }

    private static List<Job> createExampleJobs() {
        Job firstJob = new Job(1, 1455, 30);
        Job secondJob = new Job(1, 1455, 45);
        Job thirdJob = new Job(3, 1454, 60);

        return Arrays.asList(firstJob, secondJob, thirdJob);
    }
}
