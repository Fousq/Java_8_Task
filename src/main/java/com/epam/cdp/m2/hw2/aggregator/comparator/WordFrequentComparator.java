package com.epam.cdp.m2.hw2.aggregator.comparator;

import javafx.util.Pair;

import java.util.Comparator;

public class WordFrequentComparator implements Comparator<Pair<String, Long>> {

    @Override
    public int compare(Pair<String, Long> o1, Pair<String, Long> o2) {
        int frequentDiff = o1.getValue().compareTo(o2.getValue());
        if (frequentDiff == 0) {
            return o1.getKey().compareTo(o2.getKey());
        }
        return -frequentDiff;
    }
}
