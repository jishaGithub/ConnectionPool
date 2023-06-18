package com.solvd.laba.pool;


import com.solvd.laba.connection.MyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.concurrent.*;

public class MyConnectionPool {
    private static final Logger logger = LogManager.getLogger(MyConnectionPool.class);
    private final ConcurrentLinkedQueue<MyConnection> connectionPool;
    static MyConnectionPool connectionPoolObject;
    int counter=0;

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
        MyConnection connection = null;
        for (int i=1; i<=poolSize; i++) {
            connection = new MyConnection(i);
            connectionPool.offer(connection);
            logger.info("Connection ID:"+connection.getConnectionID()+" added to the connection pool");
            Thread.sleep(500);
        }
    }

    public MyConnection getConnection() throws InterruptedException {
        counter++;
        MyConnection connection = connectionPool.poll();
        if (connection != null) {
            logger.info("Request No:"+counter+". Got connection:"+connection.getConnectionID());
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