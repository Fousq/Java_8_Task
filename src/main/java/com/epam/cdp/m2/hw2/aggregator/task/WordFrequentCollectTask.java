package com.epam.cdp.m2.hw2.aggregator.task;

import com.epam.cdp.m2.hw2.aggregator.comparator.WordFrequentComparator;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class WordFrequentCollectTask extends RecursiveTask<List<Pair<String, Long>>> {
    private static final int THRESHOLD = 20;
    private static final WordFrequentComparator wordFrequentComparator = new WordFrequentComparator();
    private List<String> words;
    private long limit;

    private WordFrequentCollectTask(List<String> words) {
        this.words = words;
    }

    public WordFrequentCollectTask(List<String> words, long limit) {
        this.words = words;
        this.limit = limit;
    }

    @Override
    protected List<Pair<String, Long>> compute() {
        List<Pair<String, Long>> wordsFrequent;
        if (words.size() > THRESHOLD) {
            Collection<WordFrequentCollectTask> tasks = ForkJoinTask.invokeAll(createSubtasks());
            wordsFrequent = new ArrayList<>();
            for (WordFrequentCollectTask task : tasks) {
                merge(wordsFrequent, task.join());
            }
            wordsFrequent.sort(wordFrequentComparator);
            return wordsFrequent.size() >= limit && limit > 0 ? wordsFrequent.subList(0, (int) limit) : wordsFrequent;
        }
        wordsFrequent = collect(words);
        wordsFrequent.sort(wordFrequentComparator);
        return wordsFrequent.size() >= limit ? wordsFrequent.subList(0, (int) limit) : wordsFrequent;
    }

    private List<Pair<String, Long>> collect(List<String> words) {
        List<Pair<String, Long>> result = new ArrayList<>();
        for (String word : words) {
            if (isWordInPair(word, result)) {
                increaseWordFrequent(word, result);
            } else {
                result.add(new Pair<>(word, 1L));
            }
        }
        return result;
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

    private Collection<WordFrequentCollectTask> createSubtasks() {
        List<WordFrequentCollectTask> tasks = new ArrayList<>();
        tasks.add(new WordFrequentCollectTask(words.subList(0, words.size() / 2)));
        tasks.add(new WordFrequentCollectTask(words.subList(words.size() / 2, words.size())));
        return tasks;
    }

    private void merge(List<Pair<String, Long>> to, List<Pair<String, Long>> from) {
        for (Pair<String, Long> fromPair : from) {
            if (containsWord(to, fromPair.getKey())) {
                mergeFrequent(to, fromPair);
            } else {
                to.add(fromPair);
            }
        }
    }

    private boolean containsWord(List<Pair<String, Long>> wordsFrequent, String word) {
        for (Pair<String, Long> wordFrequent : wordsFrequent) {
            if (Objects.equals(wordFrequent.getKey(), word)) {
                return true;
            }
        }
        return false;
    }

    private void mergeFrequent(List<Pair<String, Long>> wordsFrequent, Pair<String, Long> wordFrequent) {
        for (int i = 0; i < wordsFrequent.size(); i++) {
            Pair<String, Long> pair = wordsFrequent.get(i);
            if (Objects.equals(pair.getKey(), wordFrequent.getKey())) {
                wordsFrequent.add(i, new Pair<>(wordFrequent.getKey(),
                        pair.getValue() + wordFrequent.getValue()));
                break;
            }
        }
    }
}
