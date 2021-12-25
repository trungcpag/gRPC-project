package com.app.gprc.blog.server;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import io.grpc.stub.StreamObserver;
import org.bson.Document;


public class BlogServerIml extends BlogServiceGrpc.BlogServiceImplBase {

    private MongoClient mongoClient = MongoClients.create("mongodb+srv://trung:12345@grpc.ad9xe.mongodb.net");
    private MongoDatabase database = mongoClient.getDatabase("mydb");
    private MongoCollection<Document> collection = database.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {

        System.out.println("Received Create blog request");

        Blog blog = request.getBlog();
        Document document = new Document("author_id", blog.getAuthor())
                .append("title", blog.getTitle())
                .append("content", blog.getContent());
        System.out.println("Inserting blog...");
        collection.insertOne(document);
        String id = document.getObjectId("_id").toString();
        System.out.println("Inserted blog id: " + id);
        CreateBlogResponse response = CreateBlogResponse.newBuilder()
                .setBlog(blog.toBuilder().setId(id)).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
