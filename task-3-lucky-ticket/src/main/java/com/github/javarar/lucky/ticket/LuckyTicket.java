package com.github.javarar.lucky.ticket;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class LuckyTicket {
    public static void main(String[] args) {
        System.out.println(moscowLuckyTicketProbability(6));
        System.out.println(leningradLuckyTicketProbability(6));
    }

    public static double moscowLuckyTicketProbability(int serialNumberLength) {
        var executor = Executors.newCachedThreadPool();
        AtomicInteger counter = new AtomicInteger(0);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i <= (int) (Math.pow(10, serialNumberLength) - 1); i++) {
            String currentNum = String.valueOf(i);
            String sb = "0".repeat(Math.max(0, serialNumberLength - currentNum.length())) + i;
            futures.add(executor.submit(new MoscowTicketLogic(sb, counter)));
        }
        return waitForEnd(serialNumberLength, executor, counter, futures);
    }

    public static double leningradLuckyTicketProbability(int serialNumberLength) {
        var executor = Executors.newCachedThreadPool();
        AtomicInteger counter = new AtomicInteger(0);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i <= (int) (Math.pow(10, serialNumberLength) - 1); i++) {
            String currentNum = String.valueOf(i);
            String sb = "0".repeat(Math.max(0, serialNumberLength - currentNum.length())) + i;
            futures.add(executor.submit(new LeningradTicketLogic(sb, counter)));
        }
        return waitForEnd(serialNumberLength, executor, counter, futures);
    }

    private static double waitForEnd(int serialNumberLength, ExecutorService executor, AtomicInteger counter, List<Future<?>> futures) {
        for (var f : futures) {
            try {
                f.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        executor.shutdown();
        return ((double) counter.get()) / (Math.pow(10, serialNumberLength) - 1);
    }

    private record LeningradTicketLogic(String s, AtomicInteger atomicInteger) implements Runnable {
        @Override
        public void run() {
            var leftSum = 0;
            var rightSum = 0;
            var leftIndex = 0;
            var rightIndex = s.length() - 1;
            while (leftIndex <= rightIndex) {
                var leftTmp = s.charAt(leftIndex) - '0';
                var rightTmp = s.charAt(rightIndex) - '0';
                leftSum += leftTmp;
                rightSum += rightTmp;
                leftIndex++;
                rightIndex--;
            }
            if (leftSum == rightSum) {
                atomicInteger.getAndIncrement();
            }
        }
    }

    private record MoscowTicketLogic(String s, AtomicInteger atomicInteger) implements Runnable {
        @Override
        public void run() {
            var evenSum = 0;
            var oddSum = 0;
            for (int i = 0; i < s.length(); i++) {
                var tmp = s.charAt(i) - '0';
                if (i % 2 == 0) {
                    evenSum += tmp;
                } else {
                    oddSum += tmp;
                }
            }
            if (evenSum == oddSum) {
                atomicInteger.getAndIncrement();
            }
        }
    }
}