package com.solvd.laba;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyConnectionPool {
    private static final int CONNECTION_POOL_MAX = 5;
    private final ConcurrentLinkedQueue<MyMockConnection> connectionPool;
    private static MyConnectionPool connectionPoolObject;
    public MyConnectionPool() {
        connectionPool = new ConcurrentLinkedQueue<>();
        createConnectionPool();
    }

    public static synchronized MyConnectionPool getInstance() {
        if(connectionPoolObject == null) {
            connectionPoolObject = new MyConnectionPool();
        }
        return connectionPoolObject;
    }

    public void createConnectionPool(){
        for (int i=1; i<=CONNECTION_POOL_MAX; i++) {
            MyMockConnection connection = new MyMockConnection(i);
            connection.openSocket();
            connection.performAuthentication();
            System.out.println("---Connection:"+connection.getConnectionId()+" added to connection pool----");
            connectionPool.offer(connection);
        }
    }

    public MyMockConnection getConnection() {
        if(connectionPool.isEmpty()){
            System.out.println("No available connections in the connection pool. Wait!!");
            return null;
        }
        MyMockConnection connection = connectionPool.poll();
        System.out.println("Got connection " + connection.getConnectionId() + " from the connection pool. ");
        return connection;
    }

    public void returnConnection(MyMockConnection connection) {
        if (connection != null) {
            connectionPool.offer(connection);
            System.out.print(".....Connection " + connection.getConnectionId() + " is returned back to the connection pool");
            getConnectionPoolSize();
        }
    }

    public void closeConnectionPool() throws IOException {
        for (MyMockConnection connection : connectionPool) {
            connection.closeSocket();
            connection.terminateObject();
        }
        System.out.println("Connection Pool closed");
    }

    public void getConnectionPoolSize(){
        System.out.println("-----Current available connections: "+connectionPool.size());
    }
}