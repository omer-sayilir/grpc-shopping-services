package net.sayilir.shopping.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import net.sayilir.shopping.service.OrderServiceImpl;
import net.sayilir.shopping.service.UserServiceImpl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author omersayilir
 * @Date 2024-09-06
 */
public class OrderServer {
    private static final Logger logger = Logger.getLogger(OrderServer.class.getName());

    private Server server;

    public static void main(String[] args) {
        OrderServer orderServer = new OrderServer();
        orderServer.startServer();
        try {
            orderServer.blockUntilShutdown();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Could not stop server", e);
        }
    }

    public void startServer() {
        int port = 50052;
        try {
            server = ServerBuilder.forPort(port)
                    .addService(new OrderServiceImpl())
                    .build()
                    .start();
            logger.info("Order Server started, listening on " + port);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    logger.log(Level.INFO, "Clean server shutdown in case JVM was shutdown");
                    try {
                        OrderServer.this.stopServer();
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
