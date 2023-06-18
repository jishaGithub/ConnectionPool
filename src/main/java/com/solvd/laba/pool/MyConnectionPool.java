package com.solvd.laba.pool;

import com.solvd.laba.connection.MyConnection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyConnectionPool {
    private static MyConnectionPool myConnectionPoolInstance;
    private BlockingQueue<MyConnection> connectionPool;

    public MyConnectionPool() {
        connectionPool = new LinkedBlockingQueue<>();
    }

    public static synchronized MyConnectionPool getMyConnectionPoolInstance() {
        if ( myConnectionPoolInstance == null ) {
            myConnectionPoolInstance = new MyConnectionPool();
        }
        return myConnectionPoolInstance;
    }

    public void initializePool(int poolSize) {
        for (int i=0; i<poolSize; i++) {
            MyConnection connection = createConnection();
            connectionPool.offer(connection);
        }


    }

    public MyConnection createConnection() {
        return new MyConnection();
    }

    public MyConnection getConnection() throws InterruptedException {
        if (connectionPool.isEmpty()) {
            return createConnection();
        }
        return connectionPool.take();
    }

    public void releaseConnection(MyConnection connection) {
        if ( connection != null ) {
            connectionPool.offer(connection);
        }
    }

    public void closeConnections() {
        for(MyConnection connection : connectionPool) {
            connection.closeConnection();
        }
        connectionPool.clear();
    }
}
