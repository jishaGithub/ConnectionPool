package com.solvd.laba.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.Socket;

public class MyConnection {
    private static final Logger logger = LogManager.getLogger(MyConnection.class);
    private boolean isAuthenticated = false;
    private boolean isConnectionOpen = false;
    private boolean isTaskDone = false;
    private int connectionID;
    private Socket socket;

    public MyConnection(int connectionID) {
        this.connectionID = connectionID;
    }

    public void openConnection() {
        socket = new Socket();
        logger.info("Connection No:"+this.connectionID+" opened");
        isConnectionOpen = true;
    }

    public void performAuthentication() {
        if (isConnectionOpen) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("Authentication done on connection:" + this.connectionID);
            isAuthenticated = true;
        }
        else {
            logger.info("Connection"+this.connectionID+" not opened");
        }
    }

    public Boolean performTask() {
        if (!isAuthenticated || !isConnectionOpen) {
            logger.info("Authentication not successful / Connection not open.");
        }

        logger.info("Performing action on connection:" + getConnectionID());
        try {
            long time = (long) (Math.random()*10000);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info("Action complete on Connection:" + getConnectionID());
        return isTaskDone = true;
    }

    public void closeConnection() throws IOException {
        if (!isConnectionOpen) {
            logger.info("Socket is not open");
        }
        socket.close();
        isConnectionOpen = false;
        logger.info("Connection:"+this.connectionID+" closed");
    }

    public int getConnectionID() {
        return this.connectionID;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public boolean isConnectionOpen() {
        return isConnectionOpen;
    }

    public boolean isTaskDone() {
        return isTaskDone;
    }

    @Override
    public String toString() {
        return "connectionID:" + connectionID ;
    }
}
