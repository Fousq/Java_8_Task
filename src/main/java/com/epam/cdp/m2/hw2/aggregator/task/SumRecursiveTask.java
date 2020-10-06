package com.epam.cdp.m2.hw2.aggregator.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class SumRecursiveTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 10;
    private List<Integer> numbers;

    public SumRecursiveTask(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    protected Integer compute() {
        if (numbers.size() > THRESHOLD) {
            Collection<SumRecursiveTask> tasks = ForkJoinTask.invokeAll(createSubtask());
            int sum = 0;
            for (SumRecursiveTask task : tasks) {
                sum += task.join();
            }
            return sum;
        }
        return processing(numbers);
    }

    private Integer processing(List<Integer> numbers) {
        int sum = 0;
        for (Integer number : numbers) {
            sum += number;
        }
        return sum;
    }

    private Collection<SumRecursiveTask> createSubtask() {
        List<SumRecursiveTask> tasks = new ArrayList<>();
        tasks.add(new SumRecursiveTask(numbers.subList(0, numbers.size() / 2)));
        tasks.add(new SumRecursiveTask(numbers.subList(numbers.size() / 2, numbers.size())));
        return tasks;
    }
}
