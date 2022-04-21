package io.vteial.salestap.services.impl;

import io.quarkus.scheduler.Scheduled;
import io.vteial.salestap.services.TaskService;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class DefaultTaskService implements TaskService {

    private AtomicInteger testCounter = new AtomicInteger();

    public int getTestCounter() {
        return testCounter.get();
    }

    @Scheduled(every = "10s")
    public void incrementTestCounter() {
        testCounter.incrementAndGet();
    }
}
