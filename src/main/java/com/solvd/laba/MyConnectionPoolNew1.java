package com.solvd.laba;

import java.util.concurrent.*;

public class MyConnectionPoolNew1 {
    private BlockingQueue<MyMockConnectionNew> connectionPool1;
    private ExecutorService executor;
    private static final int CONNECTION_POOL_MAX = 5;
    static MyConnectionPoolNew1 connectionPoolObject1;

    public MyConnectionPoolNew1() {
        connectionPool1 = new LinkedBlockingQueue<>(CONNECTION_POOL_MAX);
        executor = Executors.newFixedThreadPool(CONNECTION_POOL_MAX);
        createConnectionPool();
    }

    public static synchronized MyConnectionPoolNew1 getInstance() {
        if (connectionPoolObject1 == null) {
            connectionPoolObject1 = new MyConnectionPoolNew1();
        }
        return connectionPoolObject1;
    }

    public Future<MyMockConnectionNew> getConnection() {
        if (connectionPool1.isEmpty()) {
            System.out.println("No available connections in the connection pool. Wait!!");
        }
        Callable<MyMockConnectionNew> getConnectionTask = () -> {
            MyMockConnectionNew connection = connectionPool1.poll();
            if (connection != null) {
                System.out.println("Got connection " + connection.getConnectionID() + " from the connection pool.");
            }
            return connection;
        };
        return executor.submit(getConnectionTask);
    }

    public void returnConnection(MyMockConnectionNew connection) {
        if (connection != null) {
            connectionPool1.offer(connection);
            System.out.println("Connection " + connection.getConnectionID() + " is returned back to the connection pool.");
            getConnectionPoolSize();
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }

    private MyMockConnectionNew createConnection(int i) {
        return new MyMockConnectionNew(i);
    }

    public void createConnectionPool() {
        for (int i = 1; i <= CONNECTION_POOL_MAX; i++) {
            MyMockConnectionNew connection = new MyMockConnectionNew(i);
            connection.openSocket();
            connection.performAuthentication();
            System.out.println("---Connection:" + connection.getConnectionID() + " added to connection pool----");
            connectionPool1.offer(connection);
        }
    }

    public void getConnectionPoolSize() {
        System.out.print("-----Current available connections: "+connectionPool1.size()+"\n");
    }
}