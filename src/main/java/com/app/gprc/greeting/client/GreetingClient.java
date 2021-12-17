package com.app.gprc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Client gRPC");
        ManagedChannel channel= ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        System.out.println("Creating stub");

        //DummyServiceGrpc.DummyServiceBlockingStub syncClient =  DummyServiceGrpc.newBlockingStub(channel);
        //DummyServiceGrpc.DummyServiceFutureStub a
//
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
//        Greeting greeting = Greeting.newBuilder()
//                .setFirstName("Trung")
//                .setLastName("Tran")
//                .build();
//
//        GreetRequest greetRequest = GreetRequest.newBuilder().setGreeting(greeting).build();
//
//        GreetResponse greetResponse = greetClient.greet(greetRequest);
//
//        System.out.println("GReet response: " + greetResponse);

        GreetManyTimesRequest greetManyTimesRequest =  GreetManyTimesRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Trung Tran").build())
                .build();

        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });

        channel.shutdown();
    }
}
