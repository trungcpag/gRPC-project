package com.app.gprc.greeting.client;

import com.proto.greet.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Client gRPC");

        GreetingClient main = new GreetingClient();
        main.run();
    }

    public void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
//        doClientStreamingCall(channel);//
//        doBiDiStreamingCall(channel);
        doUnaryCallWithDeadline(channel);
    }

    private void doClientStreamingCall(ManagedChannel channel) {

        GreetServiceGrpc.GreetServiceStub asynclient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongGreetRequest> requestObserver = asynclient.longGreet(new StreamObserver<LongGreetReponse>() {
            @Override
            public void onNext(LongGreetReponse value) {
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                // we get a error from the server
            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending us sending");
                latch.countDown();
            }
        });

        System.out.println("send message 1");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Trung").build())
                .build());
        System.out.println("send message 2");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("John").build())
                .build());
        System.out.println("send message 3");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Marc").build())
                .build());
        requestObserver.onCompleted();
        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doServerStreamingCall(ManagedChannel channel) {
        System.out.println("Creating stub");
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Trung Tran").build())
                .build();

        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });

        channel.shutdown();
    }

    private void doBiDiStreamingCall(ManagedChannel channel) {
        System.out.println("Create stub");
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetEveryoneRequest> requestStreamObserver = asyncClient.greetEveryone(new StreamObserver<GreetEveryoneReponse>() {
            @Override
            public void onNext(GreetEveryoneReponse value) {
                System.out.println("Response from server: " + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending data");
                latch.countDown();
            }
        });

        Arrays.asList("Trung", "John", "Marc").forEach(
                name -> {
                    System.out.println("sending: " + name);
                    requestStreamObserver.onNext(GreetEveryoneRequest.newBuilder()
                            .setGreeting(Greeting.newBuilder().setFirstName(name).build()).build());
                }
        );

        requestStreamObserver.onCompleted();
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doUnaryCallWithDeadline(ManagedChannel channel) {
        System.out.println("Create stub");
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        try {
            System.out.println("send message with 5000 ms");
            GreetWithDeadlineResponse response = greetClient.withDeadline(Deadline.after(5000, TimeUnit.SECONDS))
                    .greetWithDeadline(GreetWithDeadlineRequest.newBuilder().setGreeting(
                            Greeting.newBuilder().setFirstName("Trung").getDefaultInstanceForType()
                    ).build());

            System.out.println(response);
        } catch (StatusRuntimeException e) {
            if(e.getStatus() == Status.DEADLINE_EXCEEDED){
                System.out.println("Deadline have been exceeded, we don't want the response");
            } else {
                e.printStackTrace();
            }
        }
        try {
            System.out.println("send message with 100 ms");
            GreetWithDeadlineResponse response = greetClient.withDeadline(Deadline.after(100, TimeUnit.MILLISECONDS ))
                    .greetWithDeadline(GreetWithDeadlineRequest.newBuilder().setGreeting(
                            Greeting.newBuilder().setFirstName("Trung").getDefaultInstanceForType()
                    ).build());

            System.out.println(response);
        } catch (StatusRuntimeException e) {
            if(e.getStatus() == Status.DEADLINE_EXCEEDED){
                System.out.println("Deadline have been exceeded, we don't want the response");
            } else {
                e.printStackTrace();
            }
        }
    }


}
