package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MyServer {

    private static final Logger logger = LoggerFactory.getLogger(GreetingService.class.getName());

    private Server server;
    public static void main(String[] args) {
        int port = 9099;
        Server server = ServerBuilder.forPort(port).addService(new GreetingService()).build();
        logger.info("Server started, listening on " + port);
        try {
            server.start();
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
