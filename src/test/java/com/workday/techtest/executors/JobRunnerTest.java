package com.workday.techtest.executors;

import com.workday.techtest.domain.Job;
import com.workday.techtest.domain.Report;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JobRunnerTest {

    @Test
    public void runner_shouldReturnAVersion_andShouldMatchWorkdaysVersion() {
        JobRunner runner = new JobRunnerImpl();

        assertThat(runner).isNotNull();
        assertThat(runner.version()).isEqualTo("536543A4-4077-4672-B501-3520A49549E6");
    }

    @Test
    public void runner_shouldProcessAllJobs_whenJobsSubmitted_andJobCountEqualToJobs() {
        Job firstJob = new Job(1, 1455, 30);
        Job secondJob = new Job(2, 1456, 40);
        Job thirdJob = new Job(3, 1454, 60);

        List<Job> availableJobs = Arrays.asList(firstJob, secondJob, thirdJob);

        JobQueue queue = new JobQueueImpl(availableJobs);
        JobRunnerImpl runner = new JobRunnerImpl();

        runner.runner(queue, 3);

        Report generateReport = runner.reportingRunner();

        assertThat(generateReport.getNumInvocations()).isEqualTo(3L);
        assertThat(generateReport.getNumberOfUniqueJobsProcessed()).isEqualTo(3L);
    }

    @Test
    public void runner_shouldTreatMultipleJobsAsSingleJob_whenTheyShareTheSameUniqueId() {
        final int commonUniqueId = 123;

        Job firstJobWithCommonId = new Job(1, commonUniqueId, 10);
        Job secondJobCommonId = new Job(1, commonUniqueId, 10);
        Job thirdJobCommonId = new Job(1, commonUniqueId, 10);

        List<Job> availableJobs = Arrays.asList(firstJobWithCommonId, secondJobCommonId, thirdJobCommonId);

        JobQueue queue = new JobQueueImpl(availableJobs);
        JobRunnerImpl runner = new JobRunnerImpl();

        runner.runner(queue, 3);

        Report report = runner.reportingRunner();

        assertThat(report.getNumInvocations()).isEqualTo(3L);
        assertThat(report.getNumberOfUniqueJobsProcessed()).isEqualTo(1L);
    }

    @Test
    public void runner_shouldEventuallyExhaustJobs_whenJobsSubmitted() {
        int shortDuration = 10;

        Job firstJob = new Job(1, 1111, shortDuration);
        Job secondJob = new Job(2, 2222, shortDuration);
        Job thirdJob = new Job(3, 3333, shortDuration);

        List<Job> availableJobs = Arrays.asList(firstJob, secondJob, thirdJob);

        JobQueue queue = new JobQueueImpl(availableJobs);
        JobRunnerImpl runner = new JobRunnerImpl();

        runner.runner(queue, 3);

        Report report = runner.reportingRunner();

        assertThat(report.getNumInvocations()).isEqualTo(3L);
        assertThat(report.getNumberOfUniqueJobsProcessed()).isEqualTo(3L);
        assertThat(queue.pop()).isNull();
    }

    @Test
    public void runner_shouldComplete_withOutstandingJobs_whenJobsExceedRunCount_andOneJobLeftOver() {
        int shortDuration = 10;

        Job firstJob = new Job(1, 1111, shortDuration);
        Job secondJob = new Job(2, 2222, shortDuration);
        Job thirdJob = new Job(3, 3333, shortDuration);
        Job fourthJob = new Job(4, 4444, shortDuration);

        List<Job> availableJobs = Arrays.asList(firstJob, secondJob, thirdJob, fourthJob);

        JobQueue queue = new JobQueueImpl(availableJobs);
        JobRunnerImpl runner = new JobRunnerImpl();

        runner.runner(queue, 3);

        Report report = runner.reportingRunner();

        assertThat(report.getNumInvocations()).isEqualTo(3L);
        assertThat(report.getNumberOfUniqueJobsProcessed()).isEqualTo(3L);
        assertThat(queue.pop()).isNotNull();
    }
}
