package com.app.gprc.blog.server;

import com.app.gprc.greeting.server.GreetServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class BlogServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("gRPC");
        Server server = ServerBuilder.forPort(50051)
                .addService(new BlogServerIml())
                .addService(ProtoReflectionService.newInstance())
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
