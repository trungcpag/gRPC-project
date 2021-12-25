package com.app.gprc.blog.client;

import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {
    public static void main(String[] args) {
        System.out.println("Client gRPC");

        BlogClient main = new BlogClient();
        main.run();
    }

    private void run(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
        Blog blog = Blog.newBuilder().setContent("Hello first Blog")
                .setAuthor("TruTra")
                .setTitle("New Blog")
                .build();
        CreateBlogResponse response = blogClient.createBlog(CreateBlogRequest.newBuilder().setBlog(blog).build());
        System.out.println("Received Response");
        System.out.println(response.toString());
    }
}
