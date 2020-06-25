package com.workday.techtest.executors;

import com.workday.techtest.domain.Job;

public interface JobQueue {
    Job pop();
}
