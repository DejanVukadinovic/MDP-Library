package org.example.entities;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.*;
import java.util.stream.Stream;


public class Book {
    private static final Map<Integer, Book> books = new HashMap<>();
    int id;
    private String title;
    private String author;
    private String releaseDate;
    private String language;
    private String preview;

    private Book(int id) {
        String uri = buildGutenbergURL(id);
        this.id = id;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            this.title = extractData(response.body(), "Title: (.+)");
            this.author = extractData(response.body(), "Author: (.+)");
            this.releaseDate = extractData(response.body(), "Release date: (.+) \\[eBook");
            this.language = extractData(response.body(), "Language: (.+)");
            Path path = Paths.get(String.valueOf(id));
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
            writeContents(id, response.body());
            System.out.println(getFirst100Lines(response.body()));
            this.preview = getFirst100Lines(response.body());
            downloadImage(buildGuthenbergImageUrl(id), id + "/image.jpg");
            zipDirectory(path, Paths.get(id + ".zip"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    private static String getFirst100Lines(String input) {
        // Split the string by line breaks into an array
        String[] lines = input.split("\r?\n");

        // Get the first 100 lines or the total number of lines if less than 100
        List<String> first100Lines = Arrays.asList(lines).subList(0, Math.min(100, lines.length));

        // Join the first 100 lines back into a single string
        return String.join(System.lineSeparator(), first100Lines);
    }
    private static String extractData(String data, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Not found";
    }
    private static String buildGutenbergURL(int id) {
        // Construct the URL using string concatenation
        return "https://www.gutenberg.org/cache/epub/" + id + "/pg" + id + ".txt";
    }
    private static String buildGuthenbergImageUrl(int id) {
        // Construct the URL using string concatenation
        return "https://www.gutenberg.org/cache/epub/" + id + "/pg" + id + ".cover.medium.jpg";
    }
    public static void zipDirectory(Path sourceDirPath, Path zipFilePath) throws IOException {
        // Create a ZipOutputStream to write the zip file
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath));
             // Traverse the directory
             Stream<Path> paths = Files.walk(sourceDirPath)) {

            // For each file or directory, add it to the zip output stream
            paths.filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString().replace("\\", "/"));
                        try {
                            // Put the file into the zip
                            zipOutputStream.putNextEntry(zipEntry);
                            // Write the file content to the zip
                            Files.copy(path, zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Failed to zip file: " + path);
                            e.printStackTrace();
                        }
                    });
        }
    }
    public static void downloadImage(String imageUrl, String destinationFile) throws IOException {
        // Open a stream from the URL
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            // Save the image to the destination file
            Files.copy(inputStream, Paths.get(destinationFile));
        }
    }
    private static void writeContents(int id, String data) {
        File file = new File(String.valueOf(id) + "/content.txt");

        // Using BufferedWriter to write content to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(data);
            System.out.println("File written successfully at: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("An error occurred while writing the file: " + e.getMessage());
        }
    }
    public static Book getBook(int id) {
        if (!books.containsKey(id)) {
            books.put(id, new Book(id));
        }
        return books.get(id);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getLanguage() {
        return language;
    }
    public String getContent() {
        File file = new File(String.valueOf(id) + ".txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
        return file.getAbsolutePath();
    }
    public String getPreview() {
        return preview;
    }


    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", language='" + language + '\'' +
                ", preview='"+ preview  + '\''+
                '}';
    }

}
