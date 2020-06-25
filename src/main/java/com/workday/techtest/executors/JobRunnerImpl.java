package com.workday.techtest.executors;

import com.workday.techtest.domain.Job;
import com.workday.techtest.domain.Report;

import java.util.HashSet;
import java.util.Set;

public class JobRunnerImpl implements JobRunner {

    private static final String VERSION = "536543A4-4077-4672-B501-3520A49549E6";

    private int durationTally;
    private int executions;
    private final Set<Long> uniqueJobIdsProcessed;
    private Report report;

    public JobRunnerImpl() {
        this.durationTally = 0;
        this.executions = 0;
        this.uniqueJobIdsProcessed = new HashSet<>();
        this.report = new Report();
    }

    @Override
    public void runner(JobQueue jobQueue, long jobCount) {
        while (jobCount > 0) {
            Job job = jobQueue.pop();

            jobCount -= 1;
            executions += 1;
            durationTally += job.duration();

            uniqueJobIdsProcessed.add(job.uniqueId());
        }

        report = reportingRunner();
        printReport();
    }

    public void printReport() {
        System.out.println(report.toString()); // This would be a call to a logger in the production
    }

    public Report reportingRunner() {
        report = new Report();

        report.setNumInvocations(executions);
        report.setTotalExecutionTimeInMilliseconds(durationTally);
        report.setNumberOfUniqueJobsProcessed(uniqueJobIdsProcessed.size());

        return report;
    }

    @Override
    public String version() {
        return VERSION;
    }
}
