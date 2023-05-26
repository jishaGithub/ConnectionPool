package com.solvd.laba;

import java.io.IOException;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException {
        int connectionPoolSize = 5;
        int additionalPoolSize = 2;
        MyConnectionPool myPool = MyConnectionPool.getInstance();
        Thread thread;
        for (int i = 0; i < connectionPoolSize; i++) {
            thread = new Thread(new CustomRunnable1(myPool));
            thread.start();
        }
        ExecutorService executorService = Executors.newFixedThreadPool(additionalPoolSize);
        for (int i = 0; i < additionalPoolSize; i++) {
            Thread thread1 = new Thread(new CustomRunnable1(myPool));
            System.out.print("Additional connection request -> ");
            executorService.execute(thread1);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        executorService.shutdown();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\n****** Part 2 : Connection pool using future interface ***********\n");
        int connectionPoolSize1 = 5;
        int additionalPoolSize1 = 2;
        MyConnectionPoolNew1 myPool1 = MyConnectionPoolNew1.getInstance();
        CustomThread1 thread1;
        for (int i = 0; i < connectionPoolSize1; i++) {
            thread1 = new CustomThread1(myPool1);
            thread1.start();
        }
        ExecutorService executorService1 = Executors.newFixedThreadPool(additionalPoolSize1);
        for (int i = 0; i < additionalPoolSize1; i++) {
            CustomThread1 thread2 = new CustomThread1(myPool1);
            System.out.print("\nAdditional connection request -> ");
            executorService1.execute(thread2);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        executorService1.shutdown();
        myPool1.shutdown();
    }
}
