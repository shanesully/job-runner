package com.workday.techtest.domain;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.TimerTask;

@AllArgsConstructor
@RequiredArgsConstructor
public class Job extends TimerTask {
    private long customerId;
    private long uniqueId;
    private int duration;

    public long customerId() {
        return customerId;
    }

    public long uniqueId() {
        return uniqueId;
    }

    public int duration() {
        return duration;
    }

    public void execute() {
        System.out.println("Executing job id: " + uniqueId + " for customer: " + customerId + "...");

        try {
            Thread.sleep(duration); // sleep is used as an example of where real-world work could be executed
        } catch (InterruptedException e) {
            System.out.println("Failed to process job id: " + uniqueId + " for customer: " + customerId);
        }
    }

    @Override
    public void run() {
        execute();
    }
}
