package com.workday.techtest.executors;

import com.workday.techtest.domain.Job;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JobQueueTest {

    @Test
    public void queue_shouldPopulateWithJobs_whenListOfLegalJobsSubmitted() {
        Job shortJobOne = new Job(1, 1455, 30);
        Job shortJobTwo = new Job(1, 1456, 30);
        Job shortJobThree = new Job(1, 1454, 31);

        List<Job> jobsToSubmit = Arrays.asList(shortJobOne, shortJobTwo, shortJobThree);

        JobQueueImpl queue = new JobQueueImpl(jobsToSubmit);

        Job topJob = queue.pop();

        assertThat(topJob.duration()).isEqualTo(30);
    }

    @Test
    public void queue_shouldRejectJob_ifJobDurationExceedsAllowedPerJobDuration() {
        Job longRunningJob = new Job(1, 1455, 10000);

        List<Job> jobs = Arrays.asList(longRunningJob);

        JobQueueImpl queue = new JobQueueImpl(jobs);

        assertThat(queue.pop()).isNull();
    }

    @Test
    public void queue_shouldAddUniqueJobId_toAllocationTable_whenNewJobSubmitted() {
        Job job = new Job(1, 1234, 30);

        List<Job> jobs = Arrays.asList(job);
        JobQueueImpl queue = new JobQueueImpl(jobs);

        Map<Long, Integer> jobsInQueueById = queue.getTimeAllowedForJobId();

        assertThat(jobsInQueueById.size()).isEqualTo(1);
        assertThat(jobsInQueueById).containsOnlyKeys(1234L);
        assertThat(jobsInQueueById).containsValue(30);
    }

    @Test
    public void queue_shouldNotDuplicateIds_whenSameIdSubmittedTwice() {
        Job jobOne = new Job(1, 1234, 30);
        Job jobTwo = new Job(1, 1234, 30);
        Job jobThree = new Job(1, 4321, 30);

        List<Job> jobs = Arrays.asList(jobOne, jobTwo, jobThree);

        JobQueueImpl queue = new JobQueueImpl(jobs);

        Map<Long, Integer> jobsInQueueById = queue.getTimeAllowedForJobId();

        assertThat(jobsInQueueById.size()).isEqualTo(2);
    }

    @Test
    public void queue_shouldReturnSubmittedJob_whenPopped() {
        Job submittedJob = new Job(1, 123, 30);
        JobQueueImpl queue = new JobQueueImpl(Arrays.asList(submittedJob));

        assertThat(queue.pop())
                .isNotNull()
                .isInstanceOf(Job.class)
                .hasFieldOrPropertyWithValue("customerId", 1L)
                .hasFieldOrPropertyWithValue("uniqueId", 123L)
                .hasFieldOrPropertyWithValue("duration", 30);
    }
}