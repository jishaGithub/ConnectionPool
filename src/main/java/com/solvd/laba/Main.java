package com.solvd.laba;

import com.solvd.laba.connection.MyConnection;
import com.solvd.laba.pool.MyConnectionPool;
import com.solvd.laba.threads.CustomRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {
    private static final int CONNECTION_POOL_SIZE = 5;
    private static final int THREAD_POOL_SIZE = 7;

    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        CustomRunnable customRunnable = new CustomRunnable();
        Thread thread1 = new Thread(customRunnable);
        Thread thread2 = new Thread(customRunnable);


        MyConnectionPool myConnectionPool = MyConnectionPool.getMyConnectionPoolInstance();
        myConnectionPool.initializePool(CONNECTION_POOL_SIZE);
        ExecutorService myThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        ArrayList<Future<Void>> futureTasks = new ArrayList<>();
        for(int i=0; i<THREAD_POOL_SIZE; i++) {
            Future<Void> futureTask = myThreadPool.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    MyConnection connection = myConnectionPool.getConnection();
                    logger.info("Thread:"+thread1.threadId()+" started");
                    logger.info("Got connection:"+connection.getConnectionId()+" from the connection pool");
                    connection.performAuthentication();
                    connection.performTask();
                    myConnectionPool.releaseConnection(connection);
                    return null;
                }
            });
            futureTasks.add(futureTask);
        }

        for (Future<Void> futureTask : futureTasks) {
            try {
                futureTask.get();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            } catch (ExecutionException e) {
                logger.error(e.getMessage());
            }
        }








    }
}