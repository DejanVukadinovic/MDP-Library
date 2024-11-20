package org.example.entities;

public class Book {
    private String id;
    private String title;
    private String author;
    private String preview;

    public Book(String id, String title, String author, String preview) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.preview = preview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}
