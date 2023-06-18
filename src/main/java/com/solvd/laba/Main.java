package com.solvd.laba;

import com.solvd.laba.connection.MyConnection;
import com.solvd.laba.pool.MyConnectionPool;
import com.solvd.laba.threads.MyThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {
    private static final int CONNECTION_POOL_SIZE = 5;
    private static final int THREAD_POOL_SIZE = 7;
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        MyConnectionPool myConnectionPool = MyConnectionPool.getConnectionPoolObject();
        try {
            myConnectionPool.initializePool(CONNECTION_POOL_SIZE);
        } catch (IOException | InterruptedException e) {
            logger.info(e.getMessage());
        }
        ExecutorService mySingleThreadPool = Executors.newFixedThreadPool(CONNECTION_POOL_SIZE);
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();
        CompletableFuture<Void> loadConnectionsFuture = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
                MyConnection connection = null;
                try {
                    connection = myConnectionPool.getConnection();
                    connection.openConnection();
                    connection.performAuthentication();
                    connection.performTask();
                    if(connection.isTaskDone()) {
                        myConnectionPool.returnConnection(connection);
                    }
                } catch (InterruptedException e) {
                    logger.info(e.getMessage());
                }
            }
        });
        futures.add(loadConnectionsFuture);

        for (int i = 0; i < THREAD_POOL_SIZE - CONNECTION_POOL_SIZE; i++) {
            CompletableFuture<Void> threadFuture = CompletableFuture.runAsync(() -> {
                MyThread myThread = new MyThread(Thread.currentThread().getName(), myConnectionPool);
                myThread.run();
                }, mySingleThreadPool);
            futures.add(threadFuture);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allFutures.get();
            mySingleThreadPool.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            logger.info(e.getMessage());
        }
    }
}