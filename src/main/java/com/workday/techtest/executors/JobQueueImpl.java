package com.workday.techtest.executors;

import com.workday.techtest.domain.Job;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;

@Getter
@Setter
@NoArgsConstructor
public class JobQueueImpl implements JobQueue {

    private static final int MILLISECONDS_ALLOWED_PER_JOB = 1000;

    private int totalDurationJobsSubmitted;
    private Queue<Job> pendingJobs = new LinkedList<>();
    private Map<Long, Integer> timeAllowedForJobId = new HashMap<>();

    public JobQueueImpl(List<Job> submittedJobs) {

        for (Job job : submittedJobs) {
            int duration = job.duration();
            totalDurationJobsSubmitted += duration;

            if (duration <= MILLISECONDS_ALLOWED_PER_JOB) {
                addJobToTimeAllocationsTable(job);
                pendingJobs.add(job);
            } else {
                System.out.println("Job id: " + job.uniqueId() + " exceeds legal single job duration");
            }
        }
    }

    public void process() {
        for (Job job : pendingJobs) {
            if (timeAllowedForJobId.containsKey(job.uniqueId())) {
                timeAndExecuteJob(job);
            }
        }
    }

    public int add(Job job) {
        if (job.duration() > totalDurationJobsSubmitted) {
            return 0;
        }

        pendingJobs.add(job);

        incrementDurationForJob(job);
        addJobToTimeAllocationsTable(job);

        return pendingJobs.size();
    }

    private void timeAndExecuteJob(Job job) {
        long jobId = job.uniqueId();

        int secondsRemaining = timeAllowedForJobId.get(jobId);

        Timer countdownTimer = new Timer();

        if (secondsRemaining > MILLISECONDS_ALLOWED_PER_JOB) {
            countdownTimer.schedule(job, 0, MILLISECONDS_ALLOWED_PER_JOB);
            timeAllowedForJobId.put(jobId, secondsRemaining - MILLISECONDS_ALLOWED_PER_JOB);
        } else {
            countdownTimer.schedule(job, 0, secondsRemaining);
            timeAllowedForJobId.put(jobId, MILLISECONDS_ALLOWED_PER_JOB - secondsRemaining);
        }
    }

    private void incrementDurationForJob(Job job) {
        this.totalDurationJobsSubmitted += job.duration();
    }

    private void decrementTotalDurationForJob(Job job) {
        this.totalDurationJobsSubmitted -= job.duration();
    }

    private void addJobToTimeAllocationsTable(Job job) {
        long uniqueId = job.uniqueId();

        if (!timeAllowedForJobId.containsKey(uniqueId)) {
            timeAllowedForJobId.put(job.uniqueId(), job.duration());
        }
    }

    @Override
    public Job pop() {
        if (pendingJobs.peek() != null) {
            Job currentJob = pendingJobs.remove();

            currentJob.execute();
            decrementTotalDurationForJob(currentJob);

            return currentJob;
        } else {
            return null;
        }
    }
}
