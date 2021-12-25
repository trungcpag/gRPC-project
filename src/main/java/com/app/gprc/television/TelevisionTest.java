package com.app.gprc.television;

import com.proto.television.Television;
import com.proto.television.Type;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TelevisionTest {
    public static void main(String[] args) throws IOException {

        Path pathV1 = Paths.get("tv-v1");
        Path pathV2 = Paths.get("tv-v2");

        Television television = Television.newBuilder()
                .setBrand("Sony")
                .setModel(2016)
                .setType(Type.OLED)
                .build();
//        Path pathV1 = Paths.get("tv-v1");
        Files.write(pathV2, television.toByteArray());
//        Path pathV1 = Paths.get("tv-v1");
//        byte[] bytes = Files.readAllBytes(pathV1);
//        System.out.println(Television.parseFrom(bytes));

    }
}
