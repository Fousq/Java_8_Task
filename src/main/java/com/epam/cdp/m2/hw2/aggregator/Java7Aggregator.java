package com.epam.cdp.m2.hw2.aggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.epam.cdp.m2.hw2.aggregator.comparator.WordFrequentComparator;
import javafx.util.Pair;

public class Java7Aggregator implements Aggregator {
    private static final WordFrequentComparator wordFrequentComparator = new WordFrequentComparator();

    @Override
    public int sum(List<Integer> numbers) {
        int sum = 0;
        for (Integer number : numbers) {
            sum += number;
        }
        return sum;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        List<Pair<String, Long>> result = new ArrayList<>();
        for (String word : words) {
            if (isWordInPair(word, result)) {
                increaseWordFrequent(word, result);
            } else {
                result.add(new Pair<>(word, 1L));
            }
        }
        result.sort(wordFrequentComparator);
        return result.size() >= limit ? result.subList(0, (int) limit) : result;
    }

    private void increaseWordFrequent(String word, List<Pair<String, Long>> result) {
        for (int i = 0; i < result.size(); i++) {
            if (Objects.equals(result.get(i).getKey(), word)) {
                result.set(i, new Pair<>(word, result.get(i).getValue() + 1));
            }
        }
    }

    private boolean isWordInPair(String word, List<Pair<String, Long>> pairs) {
        for (Pair<String, Long> pair : pairs) {
            if (Objects.equals(word, pair.getKey())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }
}
