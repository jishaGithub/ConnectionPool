package com.solvd.laba.threads;

import com.solvd.laba.pool.MyConnectionPool;
import com.solvd.laba.connection.MyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MyThread implements Runnable {
    private static final Logger logger = LogManager.getLogger(MyThread.class);
    private String threadName;
    private MyConnectionPool myConnectionPool;

    public MyThread(String threadName, MyConnectionPool myConnectionPool) {
        this.threadName = threadName;
        this.myConnectionPool = myConnectionPool;
    }
    @Override
    public void run() {
        MyConnection connection = null;
        try {
            connection = myConnectionPool.getConnection();
            if (connection == null) {
                logger.info("All connections are in use. Waiting for a connection.");
            }
            if(connection != null) {
                connection.openConnection();
                connection.performAuthentication();
                if (connection.isAuthenticated()) {
                    connection.performTask();
                    connection.closeConnection();
                }
            }
        } catch (InterruptedException | IOException e) {
            logger.info(e.getMessage());
        } finally {
            if (connection != null && connection.isTaskDone()) {
                myConnectionPool.returnConnection(connection);
            }
        }
        logger.info(threadName + " completed");
    }
}

