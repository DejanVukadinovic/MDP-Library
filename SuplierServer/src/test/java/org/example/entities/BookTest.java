package org.example.entities;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @org.junit.jupiter.api.Test
    void getBook() {
        Book book = Book.getBook(24022);
        Book book1 = Book.getBook(24022);
        assertSame(book, book1);
    }

    @org.junit.jupiter.api.Test
    void getTitle() {
        Book book = Book.getBook(24022);
        assertEquals("A Christmas Carol", book.getTitle());
    }

    @org.junit.jupiter.api.Test
    void getAuthor() {
        Book book = Book.getBook(24022);
        assertEquals("Charles Dickens", book.getAuthor());
    }

    @org.junit.jupiter.api.Test
    void getReleaseDate() {
        Book book = Book.getBook(24022);
        assertEquals("December 24, 2007", book.getReleaseDate());
    }

    @org.junit.jupiter.api.Test
    void getLanguage() {
        Book book = Book.getBook(24022);
        assertEquals("English", book.getLanguage());
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        Book book = Book.getBook(24022);
        assertEquals("{title='A Christmas Carol', author='Charles Dickens', releaseDate='December 24, 2007', language='English'}", book.toString());
    }

    @org.junit.jupiter.api.Test
    void getContent() {
        Book book = Book.getBook(24022);
        assertNotNull(book.getContent());
    }
}