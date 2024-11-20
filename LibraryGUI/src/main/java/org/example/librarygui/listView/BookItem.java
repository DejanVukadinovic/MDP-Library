package org.example.librarygui.listView;

public class BookItem {
    public String id;
    public String title;
    public String author;
    public String preview;

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

    public BookItem(String id, String title, String author, String preview) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.preview = preview;
    }
}
