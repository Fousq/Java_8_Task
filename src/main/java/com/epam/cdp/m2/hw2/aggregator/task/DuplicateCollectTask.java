package com.epam.cdp.m2.hw2.aggregator.task;

import com.epam.cdp.m2.hw2.aggregator.comparator.WordComparator;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class DuplicateCollectTask extends RecursiveTask<List<String>> {
    private static final int THRESHOLD = 10;
    private static WordComparator wordComparator = new WordComparator();
    private List<String> words;
    private long limit;

    private DuplicateCollectTask(List<String> words) {
        this.words = words;
    }

    public DuplicateCollectTask(List<String> words, long limit) {
        this.words = words;
        this.limit = limit;
    }

    @Override
    protected List<String> compute() {
        List<String> result;
        if (words.size() > THRESHOLD) {
            Collection<DuplicateCollectTask> tasks = ForkJoinTask.invokeAll(createSubtasks());
            Set<String> duplicates = new HashSet<>();
            for (DuplicateCollectTask task : tasks) {
               duplicates.addAll(task.join());
            }
            result = new ArrayList<>(duplicates);
            result.sort(wordComparator);
            return result.size() >= limit && limit > 0? result.subList(0, (int) limit) : result;
        }
        result = getDuplicates(words);
        result.sort(wordComparator);
        return result.size() >= limit ? result.subList(0, (int) limit) : result;
    }

    private Collection<DuplicateCollectTask> createSubtasks() {
        List<DuplicateCollectTask> tasks = new ArrayList<>();
        tasks.add(new DuplicateCollectTask(words.subList(0, words.size() / 2)));
        tasks.add(new DuplicateCollectTask(words.subList(words.size() / 2, words.size())));
        return tasks;
    }

    private List<String> getDuplicates(List<String> words) {
        Set<String> duplicates = new HashSet<>();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i).toUpperCase();
            for (int j = i + 1; j < words.size(); j++) {
                if (Objects.equals(word, words.get(j).toUpperCase())) {
                    duplicates.add(word);
                    break;
                }
            }
        }
        return new ArrayList<>(duplicates);
    }
}
