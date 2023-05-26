package com.solvd.laba;

public class CustomRunnable1 implements Runnable {
    private final MyConnectionPool connectionPool;
    public CustomRunnable1(MyConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        MyMockConnection connection = connectionPool.getConnection();
        if (connection != null) {
            Boolean isActionDone = connection.performAction();
            if (isActionDone) {
                connectionPool.returnConnection(connection);
            }
        }
    }
}
