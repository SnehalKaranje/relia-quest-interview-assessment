package com.example.rqchallenge.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {

    @Autowired
    static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode readFromFile(String filePath) throws IOException {
        String content = readFromFileToString(filePath);
        return objectMapper.readTree(content);
    }

    public static String readFromFileToString(String filePath) throws IOException {
        File resource = new ClassPathResource(filePath).getFile();
        byte[] byteArray = Files.readAllBytes(resource.toPath());
        return new String(byteArray);
    }
}
