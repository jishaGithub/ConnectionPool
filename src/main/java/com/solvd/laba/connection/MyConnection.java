package com.solvd.laba.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.Socket;

public class MyConnection {
    private static final Logger logger = LogManager.getLogger(MyConnection.class);
    private Socket socket;
    private int idTracker = 0;
    private int connectionId;
    public MyConnection() {
        this.connectionId = ++idTracker;
        this.socket = new Socket();
        logger.info("Socket opened");
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void performAuthentication() {
        logger.info("Authentication performed");
    }

    public void performTask() {
        logger.info("Task completed successfully");
    }

    public void closeConnection() {
        try {
            socket.close();
            logger.info("Connection closed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
