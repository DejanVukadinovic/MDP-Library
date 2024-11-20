package org.example.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Invoice implements Serializable {
    private List<Integer> books;
    private double total;
    private Date date;

    public Invoice(List<Integer> books, double total, Date date) {
        this.books = books;
        this.total = total;
        this.date = date;
    }

    public List<Integer> getBooks() {
        return books;
    }

    public double getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }
}
