package org.example;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.multipart.CompletedFileUpload;
import org.example.entities.Book;
import org.example.entities.BookPayload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BookManager {

    private static BookManager instance = null;
    private static final String uploadDir = "uploads";

    private List<Book> books = new ArrayList<>();
    private BookManager(){
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdir();
        }
    }
    public static BookManager getManager(){
        if(instance == null){
            instance = new BookManager();
        }
        return instance;
    }
    public HttpResponse<String> registerBook(CompletedFileUpload file, String body) {

        try {
            // Save the file to the upload directory
            Path filePath = Paths.get(uploadDir, file.getFilename());
            Files.write(filePath, file.getBytes());

            // Process the payload (here we just print it out)
            System.out.println("Received payload:");
            System.out.println("Name: " + body);

            Gson gson = new Gson();
            Book book = gson.fromJson(body, Book.class);
            System.out.println("Book:" + book.getTitle());
            books.add(book);

            return HttpResponse.ok("File uploaded successfully: " + file.getFilename());
        } catch (IOException e) {
            return HttpResponse.serverError("File upload failed: " + e.getMessage());
        }
    }

    public List<Book> getBooks(){
        return books;
    }
}
