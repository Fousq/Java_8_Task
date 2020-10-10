package com.epam.cdp.m2.hw2.aggregator;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

import com.epam.cdp.m2.hw2.aggregator.task.SumRecursiveTask;
import com.epam.cdp.m2.hw2.aggregator.task.WordFrequentCollectTask;
import javafx.util.Pair;

public class Java7ParallelAggregator implements Aggregator {
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(2);

    @Override
    public int sum(List<Integer> numbers) {
        Integer sum = forkJoinPool.invoke(new SumRecursiveTask(numbers));
        return Objects.nonNull(sum) ? sum : 0;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        return forkJoinPool.invoke(new WordFrequentCollectTask(words, limit));
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }
}
