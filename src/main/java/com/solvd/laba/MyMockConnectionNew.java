package com.solvd.laba;

import java.io.IOException;
import java.net.Socket;

public class MyMockConnectionNew {
    private boolean isAuthenticated = false;
    private boolean isSocketOpen = false;
    private boolean isTaskDone = false;
    private int connectionID;
    private Socket socket;
    private boolean isConnectionOpen;

    public MyMockConnectionNew(int connectionID) {
        this.connectionID = connectionID;
        isConnectionOpen = true;
    }

    public void performAuthentication() {
        if(isSocketOpen) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.print("---Authentication Done---");
            isAuthenticated = true;
        } else {
            System.out.println("Error! Socket not open!");
        }
    }

    public void openSocket() {
        socket = new Socket();
        System.out.print("---Socket is opened---");
        isSocketOpen = true;
    }

    public boolean performTask() {
        if (!isAuthenticated || !isSocketOpen) {
            System.out.println("Authentication not successful / Socket not open.");
            return isTaskDone;
        }
        System.out.print("\nPerforming action on connection " + getConnectionID() + "......\n");
        try {
            long time = (long) (Math.random()*5000);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.print("\nAction complete on Connection:" + getConnectionID() +"\n");
        return isTaskDone = true;
    }

    public void closeSocket() throws IOException {
        if (!isSocketOpen) {
            System.out.println("Socket is not open");
        } else {
            socket.close();
            isSocketOpen = false;
            System.out.println("Socket Closed");
        }
    }

    public void closeConnection() {
        if (!isConnectionOpen) {
            System.out.println("Connection not open");
        } else {
            isConnectionOpen = false;
        }
    }
    public int getConnectionID() {
        return this.connectionID;
    }
}