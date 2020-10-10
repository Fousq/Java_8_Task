package com.epam.cdp.m2.hw2.aggregator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.epam.cdp.m2.hw2.aggregator.comparator.WordFrequentComparator;
import javafx.util.Pair;

public class Java8ParallelAggregator implements Aggregator {
    private static final WordFrequentComparator wordFrequentComparator = new WordFrequentComparator();

    @Override
    public int sum(List<Integer> numbers) {
        return numbers.parallelStream().reduce(0, Integer::sum);
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        return words.parallelStream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().parallelStream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .sorted(wordFrequentComparator)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }
}
