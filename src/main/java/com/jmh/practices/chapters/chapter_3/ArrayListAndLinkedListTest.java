package com.jmh.practices.chapters.chapter_3;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ArrayListAndLinkedListTest
{
    @Param({"1000", "2000000"})
    int length;

    int[] intArray;
    Integer[] integerArray;
    ArrayList<Integer> arrayList;
    LinkedList<Integer> linkedList;

    @Setup(Level.Trial)
    public void setup()
    {
        intArray = new int[length];
        integerArray = new Integer[length];
        arrayList = new ArrayList<>(length);
        linkedList = new LinkedList<>();

        final Random random = new Random();
        for (int i = 0; i < length; i++)
        {
            final int value = random.nextInt();
            intArray[i] = value;
            arrayList.add(value);
            linkedList.add(value);
        }

        Collections.shuffle(arrayList);
        Collections.shuffle(linkedList);
        integerArray = arrayList.toArray(integerArray);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void intArray(Blackhole bh)
    {
        for(int i=0; i< intArray.length; i++){
            bh.consume(intArray[i]);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void integerArray(Blackhole bh)
    {

        for(int i=0; i< integerArray.length; i++){
            bh.consume(integerArray[i]);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void arrayList(Blackhole bh) {

        for(Integer i : arrayList) {
            bh.consume(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void linkedList(Blackhole bh)
    {
        for(Integer i : linkedList) {
            bh.consume(i);
        }
    }

    public static void main(String... args) throws RunnerException {
        Options opts = new OptionsBuilder()
            .include("ArrayListAndLinkedListTest")
            .warmupIterations(1)
            .measurementIterations(1)
            .jvmArgs("-Xms2g", "-Xmx2g")
            .shouldDoGC(true)
            .forks(1)
            .build();

        new Runner(opts).run();
    }
}

