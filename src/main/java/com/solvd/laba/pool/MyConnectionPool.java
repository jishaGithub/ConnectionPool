package com.solvd.laba.pool;


import com.solvd.laba.connection.MyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.concurrent.*;

public class MyConnectionPool {
    private static final Logger logger = LogManager.getLogger(MyConnectionPool.class);
    private final ConcurrentLinkedQueue<MyConnection> connectionPool;
    private static MyConnectionPool connectionPoolObject;
    private int counter = 0;

    public MyConnectionPool() {
        connectionPool = new ConcurrentLinkedQueue<>();
    }

    public static synchronized MyConnectionPool getConnectionPoolObject() {
        if (connectionPoolObject == null) {
            connectionPoolObject = new MyConnectionPool();
        }
        return connectionPoolObject;
    }

    public void initializePool(int poolSize) throws IOException, InterruptedException {
        for (int i=1; i<=poolSize; i++) {
            connectionPool.offer(new MyConnection(i));
            logger.info(String.format("Connection ID: %d added to the connection pool",i));
            Thread.sleep(500);
        }
    }

    public MyConnection getConnection() throws InterruptedException {
        counter++;
        MyConnection connection = connectionPool.poll();
        if (connection != null) {
            logger.info(String.format("Request No: %d Got connection: %d",counter,connection.getConnectionID()));
            Thread.sleep(500);
        }
        return connection;
    }

    public void returnConnection(MyConnection connection) {
        connectionPool.offer(connection);
        logger.info("Connection " + connection.getConnectionID() + " is returned back to the connection pool. Current pool size:"+
                getConnectionPoolSize());
    }
    public int getConnectionPoolSize() {
        return connectionPool.size();
    }

    public void shutDownPool() throws IOException {
        for(int i=0; i<connectionPool.size(); i++) {
            connectionPool.poll().closeConnection();
        }
    }
}