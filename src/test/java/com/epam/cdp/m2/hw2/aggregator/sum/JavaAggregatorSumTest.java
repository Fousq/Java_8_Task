package com.epam.cdp.m2.hw2.aggregator.sum;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runners.Parameterized;

import com.epam.cdp.m2.hw2.aggregator.Aggregator;

// TODO: Use MicroBenchmark to test the performance
public abstract class JavaAggregatorSumTest {

    @Parameterized.Parameter(0)
    public List<Integer> numbers;

    @Parameterized.Parameter(1)
    public int expected;

    private Aggregator aggregator;

    public JavaAggregatorSumTest(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{asList(1, 2, 3, 4, 5, 6, 7, 8), 36});
        data.add(new Object[]{asList(10, -10, 3), 3});
        data.add(new Object[]{emptyList(), 0});
        data.add(new Object[] {IntStream.range(0, 1_000_000).boxed().collect(Collectors.toList()),
                IntStream.range(0, 1_000_000).sum()});
        return data;
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        int actual = aggregator.sum(numbers);
        long end = System.currentTimeMillis();
        assertEquals(expected, actual);
        System.out.println("The " + aggregator.getClass()
                + " class takes " + (end - start) + " ml sec to execute sum for array of numbers with size "
                + numbers.size());
    }
}