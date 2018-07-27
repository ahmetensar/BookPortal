package com.obss.javasummerschool.bookportal.util;

import com.obss.javasummerschool.bookportal.model.Author;
import com.obss.javasummerschool.bookportal.model.Book;
import com.obss.javasummerschool.bookportal.payload.response.AuthorResponse;
import com.obss.javasummerschool.bookportal.payload.response.AuthorSummary;
import com.obss.javasummerschool.bookportal.payload.response.BookResponse;
import com.obss.javasummerschool.bookportal.payload.response.BookSummary;

import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {

    private ModelMapper() {
    }

    public static BookResponse mapBookToBookResponse(Book book) {
         BookResponse bookResponse = new BookResponse();
         bookResponse.setId(book.getId());
         bookResponse.setTitle(book.getTitle());
         bookResponse.setDescription(book.getDescription());
         bookResponse.setImageLink(book.getImageLink());

         List<AuthorSummary> authors = book.getAuthors().stream().map(author -> {
             AuthorSummary authorSummary = new AuthorSummary();
             authorSummary.setId(author.getId());
             authorSummary.setName(author.getName());
             return authorSummary;
         }).collect(Collectors.toList());
         bookResponse.setAuthors(authors);

         return bookResponse;
    }

    public static AuthorResponse mapAuthorToAuthorResponse(Author author) {
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setId(author.getId());
        authorResponse.setName(author.getName());
        authorResponse.setDescription(author.getDescription());

        List<BookSummary> books = author.getBooks().stream().map(book -> {
            BookSummary bookSummary = new BookSummary();
            bookSummary.setId(book.getId());
            bookSummary.setTitle(book.getTitle());
            bookSummary.setImageLink(book.getImageLink());
            return bookSummary;
        }).collect(Collectors.toList());
        authorResponse.setBooks(books);

        return authorResponse;
    }
}
