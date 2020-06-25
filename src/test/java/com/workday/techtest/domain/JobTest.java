package com.workday.techtest.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JobTest {

    @Test
    public void job_shouldReturnCustomerId_whenCreated() {
        Job job = new Job(1, 1234, 30);

        assertThat(job).hasFieldOrPropertyWithValue("customerId", 1L)
                .hasFieldOrPropertyWithValue("uniqueId", 1234L)
                .hasFieldOrPropertyWithValue("duration", 30);
    }
}