package com.solvd.laba;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CustomThread1 extends Thread {
    private MyConnectionPoolNew1 connectionPoolNew;
    public CustomThread1(MyConnectionPoolNew1 connectionPoolNew) {
        this.connectionPoolNew = connectionPoolNew;
    }

    @Override
    public void run() {
        Future<MyMockConnectionNew> futureConnection = connectionPoolNew.getConnection();
        MyMockConnectionNew connection = null;
        try {
            connection = futureConnection.get();
        } catch (InterruptedException | ExecutionException | NullPointerException e) {
            e.printStackTrace();
        }
        if (connection != null) {
                Boolean isActionDone = connection.performTask();
                if (isActionDone) {
                    connectionPoolNew.returnConnection(connection);
                }
            }
    }
}
