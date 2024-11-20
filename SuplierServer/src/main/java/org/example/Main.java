package org.example;

import org.example.entities.Book;
import org.example.router.Router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class Main {
    private static int serverPort = 3000;
    public static void main(String[] args){
        System.out.println("Hello World!");
        try(ServerSocket ss = new ServerSocket(serverPort)){
            Router router = Router.getRouter();
            initRoutes();
            while (true) {
                Socket socket = ss.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                String line = in.readLine();
                System.out.println(line);
                out.print(router.getResponse(line));
                out.flush();
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private static void initRoutes(){
        Router router = Router.getRouter();
        router.registerRoute("/test", (String s) -> "Hello World");
        router.registerRoute("book/title", (String s) -> {
            int id = Integer.parseInt(s);
            Book book = Book.getBook(id);
            return book.getTitle();
        });
        router.registerRoute("book/author", (String s) -> {
            int id = Integer.parseInt(s);
            Book book = Book.getBook(id);
            return book.getAuthor();
        });
        router.registerRoute("book/releaseDate", (String s) -> {
            int id = Integer.parseInt(s);
            Book book = Book.getBook(id);
            return book.getReleaseDate();
        });
        router.registerRoute("book/language", (String s) -> {
            int id = Integer.parseInt(s);
            Book book = Book.getBook(id);
            return book.getLanguage();
        });
        router.registerRoute("book/content", (String s) -> {
            int id = Integer.parseInt(s);
            Book book = Book.getBook(id);
            return book.getContent();
        });
        router.registerRoute("book/send", (String s) -> {
            int id = Integer.parseInt(s);
            Book book = Book.getBook(id);
            Gson gson = new Gson();
            try {
                sendFileWithPayload(s+".zip", gson.toJson(book));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return book.getTitle();
        });
    }
    public static void sendFileWithPayload(String filePath, String body) throws IOException {
        // Create the HTTP client
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create the HTTP POST request
            HttpPost uploadFile = new HttpPost("http://localhost:8080/libraryserver/registerBook");

            // Prepare the multipart entity
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("body", body);
            builder.addBinaryBody("file", new File(filePath));

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            // Send the request
            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                HttpEntity responseEntity = response.getEntity();
                System.out.println(EntityUtils.toString(responseEntity));
            }
        }
    }
}