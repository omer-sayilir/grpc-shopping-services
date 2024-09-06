package net.sayilir.shopping.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import net.sayilir.shopping.service.UserServiceImpl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author omersayilir
 * @Date 2024-09-06
 */
public class UserServer {
    private static final Logger logger = Logger.getLogger(UserServer.class.getName());

    private Server server;

    public static void main(String[] args) {
        UserServer userServer = new UserServer();
        userServer.startServer();
        try {
            userServer.blockUntilShutdown();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Could not stop server", e);
        }
    }

    public void startServer() {
        int port = 50051;
        try {
            server = ServerBuilder.forPort(port)
                    .addService(new UserServiceImpl())
                    .build()
                    .start();
            logger.info("Server started, listening on " + port);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    logger.log(Level.INFO, "Clean server shutdown in case JVM was shutdown");
                    try {
                        UserServer.this.stopServer();
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "Server shutdown interrupted", e);
                    }
                }
            });

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not start server", e);
        }
    }

    public void stopServer() throws InterruptedException {
        if (server != null)
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null)
            server.awaitTermination();
    }
}
