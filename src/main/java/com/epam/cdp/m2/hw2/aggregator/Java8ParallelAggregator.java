package com.epam.cdp.m2.hw2.aggregator;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.epam.cdp.m2.hw2.aggregator.comparator.WordComparator;
import com.epam.cdp.m2.hw2.aggregator.comparator.WordFrequentComparator;
import javafx.util.Pair;

public class Java8ParallelAggregator implements Aggregator {
    private static final WordFrequentComparator wordFrequentComparator = new WordFrequentComparator();
    private static final WordComparator wordComparator = new WordComparator();

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
        return words.parallelStream()
                .map(String::toUpperCase)
                .distinct()
                .filter(word -> words.stream()
                        .map(String::toUpperCase)
                        .filter(word2 -> Objects.equals(word, word2))
                        .count() > 1)
                .sorted(wordComparator)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
