package com.app.gprc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("gRPC");
        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .build();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            System.out.println("Received shutdown request");
            server.shutdown();
            System.out.println("Shutdown server");
        }));
        server.awaitTermination();
    }
}
