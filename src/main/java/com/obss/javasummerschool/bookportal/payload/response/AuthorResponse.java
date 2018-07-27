package com.obss.javasummerschool.bookportal.payload.response;

import java.util.List;

public class AuthorResponse {
    private Long id;
    private String name;
    private String description;
    private List<BookSummary> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BookSummary> getBooks() {
        return books;
    }

    public void setBooks(List<BookSummary> books) {
        this.books = books;
    }
}
